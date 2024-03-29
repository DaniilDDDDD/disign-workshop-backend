package com.workshop.contentservice.service;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import com.workshop.contentservice.dto.PaginatedResponse;
import com.workshop.contentservice.dto.sketch.SketchCreate;
import com.workshop.contentservice.dto.sketch.SketchRetrieve;
import com.workshop.contentservice.dto.sketch.SketchUpdate;
import com.workshop.contentservice.repository.TagRepository;
import com.workshop.contentservice.repository.sketch.SketchRepository;
import com.workshop.contentservice.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "sketch")
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


    @Cacheable
    public PaginatedResponse<SketchRetrieve> findAllPublicByTagsAndName(
            List<String> tagsNames, List<String> name, int page, int size, String sort
    ) {
        Page<Sketch> results;
        if (!tagsNames.isEmpty() && !name.isEmpty()) {
            List<Tag> tags = tagRepository.findAllByNameIn(tagsNames);
            results = sketchRepository.findAllByTagsAndName(
                    tags,
                    name,
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            );
        } else if (!tagsNames.isEmpty()) {
            List<Tag> tags = tagRepository.findAllByNameIn(tagsNames);
            results = sketchRepository.findAllByTagsAndAccess(
                    tags,
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            );
        } else if (!name.isEmpty()) {
            results = sketchRepository.findAllByNameAndAccess(
                    name,
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            );
        } else
            results = sketchRepository.findAllByAccess(
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            );
        return new PaginatedResponse<>(
                results.stream().map(SketchRetrieve::parseSketchPublic).collect(Collectors.toList()),
                results.getTotalElements()
        );
    }


    @Cacheable(key = "#id")
    public Sketch findById(String id) throws EntityNotFoundException {
        Optional<Sketch> sketch = sketchRepository.findById(id);
        if (sketch.isEmpty())
            throw new EntityNotFoundException("Sketch with provided id not found!");
        return sketch.get();
    }


    @Cacheable(key = "#authorEmail")
    public PaginatedResponse<SketchRetrieve> findAllByAuthorEmail(
            String authorEmail, int page, int size, String sort
    ) {
        Page<Sketch> results = sketchRepository.findAllByAuthorEmail(
                authorEmail, PageRequest.of(page, size, Sort.by(sort))
        );
        return new PaginatedResponse<>(
                results.stream().map(SketchRetrieve::parseSketchPrivate).collect(Collectors.toList()),
                results.getTotalElements()
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

    @CachePut(key = "#id")
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
                Access.getByName(sketchUpdate.getAccess()) : sketch.getAccess());
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


    @CacheEvict(key = "#id")
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


    public Resource getResource(String url) throws FileNotFoundException {
        try {
            Path filePath = Path.of(url);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException(
                        "Could not read file: " + url);
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + url);
        }
    }
}
