package com.workshop.metadataservice.security;


import com.workshop.metadataservice.repository.SketchCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SketchCommentService {

    private final SketchCommentRepository sketchCommentRepository;

    @Autowired
    public SketchCommentService(SketchCommentRepository sketchCommentRepository) {
        this.sketchCommentRepository = sketchCommentRepository;
    }
}
