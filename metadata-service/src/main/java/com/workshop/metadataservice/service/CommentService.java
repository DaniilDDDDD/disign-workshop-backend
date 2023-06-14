package com.workshop.metadataservice.service;


import com.workshop.metadataservice.document.metadata.Comment;
import com.workshop.metadataservice.dto.EntityCount;
import com.workshop.metadataservice.dto.comment.CommentCreate;
import com.workshop.metadataservice.dto.comment.CommentUpdate;
import com.workshop.metadataservice.repository.content.SketchRepository;
import com.workshop.metadataservice.repository.metadata.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.*;

@Service
@CacheConfig(cacheNames = "comment")
public class CommentService {

    private final CommentRepository commentRepository;
    private final SketchRepository sketchRepository;

    @Autowired
    public CommentService(
            CommentRepository commentRepository,
            SketchRepository sketchRepository
    ) {
        this.commentRepository = commentRepository;
        this.sketchRepository = sketchRepository;
    }


    @Cacheable
    public List<EntityCount> count(Set<String> sketches) {
        return commentRepository.countSketchesComments(sketches);
    }


    @Cacheable(key = "#sketch")
    public List<Comment> list(String sketch) {
        return commentRepository.findAllBySketch(sketch);
    }


    public Comment create(
            String sketch,
            Authentication authentication,
            CommentCreate commentCreate
    ) throws EntityExistsException {
        if (!sketchRepository.existsById(sketch))
            throw new EntityExistsException("Sketch with provided id does not exist!");

        Comment comment = Comment.builder()
                .sketch(sketch)
                .user((String) authentication.getPrincipal())
                .date(new Date())
                .text(commentCreate.getText())
                .build();
        return commentRepository.save(comment);
    }


    public Comment update(
            String id,
            Authentication authentication,
            CommentUpdate update
    ) throws EntityNotFoundException, AccessDeniedException {
        Optional<Comment> commentData = commentRepository.findById(id);
        if (commentData.isEmpty())
            throw new EntityNotFoundException("No comment with provided id!");

        Comment comment = commentData.get();

        if (!Objects.equals(comment.getUser(), authentication.getPrincipal()))
            throw new AccessDeniedException("Access denied!");

        comment.setText(update.getText());

        return commentRepository.save(comment);
    }


    public void delete(
            String id,
            Authentication authentication
    ) throws EntityNotFoundException, AccessDeniedException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty())
            throw new EntityNotFoundException("No comment with provided id!");
        if (!Objects.equals(comment.get().getUser(), authentication.getPrincipal()))
            throw new AccessDeniedException("Access denied!");
        commentRepository.delete(comment.get());
    }
}
