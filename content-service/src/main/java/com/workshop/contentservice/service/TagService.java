package com.workshop.contentservice.service;

import com.workshop.contentservice.document.Tag;
import com.workshop.contentservice.dto.tag.TagCreate;
import com.workshop.contentservice.dto.tag.TagUpdate;
import com.workshop.contentservice.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }


    public Tag getById(String id) throws EntityNotFoundException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty())
            throw new EntityNotFoundException("Tag with provided name exists!");
        return tag.get();
    }


    public List<Tag> findAll() {
        return tagRepository.findAll();
    }


    public Tag create(TagCreate tagCreate) throws EntityExistsException {
        Optional<Tag> tag = tagRepository.findByName(tagCreate.getName());
        if (tag.isPresent())
            throw new EntityExistsException("Tag with provided name exists!");
        return tagRepository.save(
                Tag.builder()
                        .name(tagCreate.getName())
                        .build()
        );
    }


    public Tag update(String id, TagUpdate tagUpdate) throws EntityNotFoundException {
        Optional<Tag> tagData = tagRepository.findById(id);
        if (tagData.isEmpty())
            throw new EntityNotFoundException("Tag with provided id does not exist!");

        Tag tag = tagData.get();
        tag.setName(tagUpdate.getName());

        return tagRepository.save(tag);
    }


    public void delete(String id) throws EntityNotFoundException {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isEmpty())
            throw new EntityNotFoundException("Tag with provided id does not exist!");
        tagRepository.delete(tag.get());
    }

}
