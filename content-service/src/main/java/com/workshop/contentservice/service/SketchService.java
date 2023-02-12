package com.workshop.contentservice.service;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import com.workshop.contentservice.dto.sketch.SketchCreate;
import com.workshop.contentservice.dto.sketch.SketchUpdate;
import com.workshop.contentservice.repository.TagRepository;
import com.workshop.contentservice.repository.sketch.SketchRepository;
import com.workshop.contentservice.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;

@Service
public class SketchService {

    @Value("${filesRoot}")
    private String filesRoot;

    @Value("${maxFilesAmount}")
    private int maxFilesAmount;

    private final SketchRepository sketchRepository;
    private final TagRepository tagRepository;

    @Autowired
    public SketchService(
            SketchRepository sketchRepository,
            TagRepository tagRepository) {
        this.sketchRepository = sketchRepository;
        this.tagRepository = tagRepository;
    }


    public List<Sketch> findAllPublicByTagsAndName(
            List<String> tagsNames, List<String> name, int page, int size, String sort) {

        if (!tagsNames.isEmpty() && !name.isEmpty()) {
            List<Tag> tags = tagRepository.findAllByNameIn(tagsNames);

            return sketchRepository.findAllByTagsAndName(
                    tags,
                    name,
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            );
        }

        if (!tagsNames.isEmpty()) {
            List<Tag> tags = tagRepository.findAllByNameIn(tagsNames);

            return sketchRepository.findAllByTagsAndAccess(
                    tags,
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            );
        }

        if (!name.isEmpty()) {
            return sketchRepository.findAllByNameAndAccess(
                    name,
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            );
        }

        return sketchRepository.findAllByAccess(
                Access.PUBLIC,
                PageRequest.of(page, size, Sort.by(sort))
        );
    }


    public Sketch findById(String id) throws EntityNotFoundException {
        Optional<Sketch> sketch = sketchRepository.findById(id);
        if (sketch.isEmpty())
            throw new EntityNotFoundException("Sketch with provided id not found!");
        return sketch.get();
    }


    public List<Sketch> findAllByAuthorEmail(
            String authorEmail, int page, int size, String sort
    ) {
        return sketchRepository.findAllByAuthorEmail(
                authorEmail, PageRequest.of(page, size, Sort.by(sort))
        );
    }

    public Sketch create(
            SketchCreate sketchCreate, Authentication authentication
    ) throws EntityExistsException, IllegalArgumentException, IOException {

        if (sketchRepository.findByName(sketchCreate.getName()).isPresent())
            throw new EntityExistsException("Sketch with provided name exists!");

        Map<String, String> credentials = (Map<String, String>) authentication.getCredentials();

        Sketch sketch = Sketch.builder()
                .authorName(credentials.get("username"))
                .authorEmail(credentials.get("email"))
                .access(sketchCreate.getAccess() != null ?
                        Access.getByName(sketchCreate.getAccess()) : Access.PUBLIC)
                .name(sketchCreate.getName())
                .description(sketchCreate.getDescription())
                .tags(tagRepository.findAllByNameIn(sketchCreate.getTags()))
                .publicationDate(new Date())
                .build();

        if (sketchCreate.getFiles() != null) {

            if (sketchCreate.getFiles().size() > maxFilesAmount)
                throw new IllegalArgumentException("Amount of files must not be more then " + maxFilesAmount + "!");

            String uploadDir = this.filesRoot + sketch.getAuthorEmail() + "/";
            List<String> files = new ArrayList<>();
            for (MultipartFile multipartFile : sketchCreate.getFiles()) {
                String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUtil.saveFile(
                        uploadDir,
                        filename,
                        multipartFile
                );
                files.add(uploadDir + filename);
            }
            sketch.setFiles(files);
        }

        return sketchRepository.save(sketch);
    }


    public Sketch update(
            String id,
            SketchUpdate sketchUpdate,
            Authentication authentication
    ) throws EntityNotFoundException, AccessDeniedException, IOException {

        Optional<Sketch> sketchData = sketchRepository.findById(id);
        if (sketchData.isEmpty()) throw new EntityNotFoundException("Sketch with provided id does not exist!");

        Sketch sketch = sketchData.get();

        Map<String, String> credentials = (Map<String, String>) authentication.getCredentials();
        if (!Objects.equals(sketch.getAuthorEmail(), credentials.get("email")))
            throw new AccessDeniedException("Access denied!");

        sketch.setAccess(sketchUpdate.getAccess() != null ?
                Access.valueOf(sketchUpdate.getAccess()) : sketch.getAccess());
        sketch.setTags(sketchUpdate.getTags() != null ?
                tagRepository.findAllByNameIn(sketchUpdate.getTags()) : sketch.getTags());
        sketch.setName(sketchUpdate.getName() != null ?
                sketchUpdate.getName() : sketch.getName());
        sketch.setDescription(sketchUpdate.getDescription() != null ?
                sketchUpdate.getDescription() : sketch.getDescription());

        if (sketchUpdate.getFiles() != null) {

            if (sketchUpdate.getFiles().size() > maxFilesAmount)
                throw new IllegalArgumentException("Amount of files must not be more then " + maxFilesAmount + "!");

            for (String filename : sketch.getFiles())
                FileUtil.deleteFile(filename);

            String uploadDir = this.filesRoot + sketch.getAuthorEmail() + "/";
            List<String> files = new ArrayList<>();
            for (MultipartFile multipartFile : sketchUpdate.getFiles()) {
                String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUtil.saveFile(
                        uploadDir,
                        filename,
                        multipartFile
                );
                files.add(uploadDir + filename);
            }
            sketch.setFiles(files);
        }

        return sketchRepository.save(sketch);
    }


    public void delete(
            String id,
            Authentication authentication
    ) throws EntityNotFoundException {

        Optional<Sketch> sketchData = sketchRepository.findById(id);
        if (sketchData.isEmpty()) throw new EntityNotFoundException("Sketch with provided id does not exist!");

        Sketch sketch = sketchData.get();
        Map<String, String> credentials = (Map<String, String>) authentication.getCredentials();
        if (!Objects.equals(sketch.getAuthorEmail(), credentials.get("email")))
            throw new AccessDeniedException("Access denied!");

        sketchRepository.delete(sketch);
    }
}
