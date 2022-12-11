package com.workshop.metadataservice.service;


import com.workshop.metadataservice.document.Comment;
import com.workshop.metadataservice.repository.CommentRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    public List<Comment> list(String sketch) {
        return commentRepository.findAllBySketch(sketch);
    }

    public List<Pair<String,Long>> count(List<String> sketches) {
        
    }
}
