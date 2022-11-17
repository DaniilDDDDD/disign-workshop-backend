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

@Service
public class SketchService {

    private final SketchRepository sketchRepository;
    private final TagRepository tagRepository;

    @Autowired
    public SketchService(SketchRepository sketchRepository, TagRepository tagRepository) {
        this.sketchRepository = sketchRepository;
        this.tagRepository = tagRepository;
    }

    public Sketch findByNamePublic(String name) throws EntityNotFoundException {
        Optional<Sketch> sketch = sketchRepository.findAllByNameAndAccess(name, Access.PUBLIC);
        if (sketch.isEmpty())
            throw new EntityNotFoundException("No sketch with provided name!");
        return sketch.get();
    }

    public List<Sketch> findAllPublic(int page, int size, String sort) {
        return sketchRepository.findAllByAccess(
                Access.PUBLIC,
                PageRequest.of(page, size, Sort.by(sort))
        ).getContent();
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

}
