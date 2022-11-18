package com.workshop.contentservice.service;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import com.workshop.contentservice.repository.SketchRepository;
import com.workshop.contentservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class SketchService {

    private final SketchRepository sketchRepository;
    private final TagRepository tagRepository;

    @Autowired
    public SketchService(
            SketchRepository sketchRepository,
            TagRepository tagRepository
    ) {
        this.sketchRepository = sketchRepository;
        this.tagRepository = tagRepository;
    }

    public List<Sketch> findAllPublic(int page, int size, String sort) {
        return sketchRepository.findAllByAccess(
                Access.PUBLIC,
                PageRequest.of(page, size, Sort.by(sort))
        ).getContent();
    }

    public Sketch findPublicById(String id) throws EntityNotFoundException {
        Optional<Sketch> sketch = sketchRepository.findById(id);
        if (sketch.isEmpty())
            throw new EntityNotFoundException("Sketch with provided id not found!");
        return sketch.get();
    }

    public List<Sketch> findPublicByName(
            List<String> name, int page, int size, String sort
    ) {
        if (name.size() == 0)
            return List.of();
        if (name.size() == 1)
            return sketchRepository.findAllByNameAndAccess(
                    name.get(0),
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            ).getContent();
        else {
            return sketchRepository.findAllByNameContainsAndAccess(
                    name,
                    Access.PUBLIC,
                    PageRequest.of(page, size, Sort.by(sort))
            ).getContent();
        }
    }

    public List<Sketch> findAllPublicByNameContains(
            List<String> name,
            int page, int size, String sort
    ) {
        return sketchRepository.findAllByNameContainsAndAccess(
                name,
                Access.PUBLIC,
                PageRequest.of(page, size, Sort.by(sort))
        ).getContent();
    }

    public List<Sketch> findAllPublicByTagContains(
            List<String> tagNames,
            int page, int size, String sort
    ) {

        List<Tag> tags = tagRepository.findAllByNameIn(tagNames);

        return sketchRepository.findAllByTagsContainsAndAccess(
                tags,
                Access.PUBLIC,
                PageRequest.of(page, size, Sort.by(sort))
        ).getContent();
    }


    public List<Sketch> findAllByAuthor()

}
