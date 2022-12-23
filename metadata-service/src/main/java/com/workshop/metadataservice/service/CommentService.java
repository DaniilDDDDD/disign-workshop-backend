package com.workshop.metadataservice.service;


import com.workshop.metadataservice.document.Comment;
import com.workshop.metadataservice.dto.comment.CommentCreate;
import com.workshop.metadataservice.dto.comment.CommentUpdate;
import com.workshop.metadataservice.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    // TODO: refactor with aggregation function on persistence level
    public Map<String, Long> count(List<String> sketches) {
        return sketches.stream().collect(
                Collectors.toMap(
                        e -> e,
                        commentRepository::countAllBySketch
                ));
    }


    public List<Comment> list(String sketch) {
        return commentRepository.findAllBySketch(sketch);
    }


    public Comment create(
            String sketch,
            Authentication authentication,
            CommentCreate commentCreate
    ) {
        // TODO: add synchronization with content microservice via message broker
        // to figure out if sketch does not exists (and throw error)
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

        if (Objects.equals(comment.getUser(), (String) authentication.getPrincipal()))
            throw new AccessDeniedException("Access denied!");

        comment.setText(update.getText());

        return commentRepository.save(comment);
    }


    public void delete(
            String id,
            Authentication authentication
    ) throws EntityNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty())
            throw new EntityNotFoundException("No comment with provided id!");
        commentRepository.delete(comment.get());
    }
}
