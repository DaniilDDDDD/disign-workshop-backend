package com.workshop.metadataservice.security;

import com.workshop.metadataservice.repository.SketchReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SketchReviewService {

    private final SketchReviewRepository sketchReviewRepository;

    @Autowired
    public SketchReviewService(SketchReviewRepository sketchReviewRepository) {
        this.sketchReviewRepository = sketchReviewRepository;
    }
}
