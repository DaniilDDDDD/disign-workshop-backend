package com.workshop.metadataservice.security;


import com.workshop.metadataservice.repository.SketchLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SketchLikeService {

    private final SketchLikeRepository sketchLikeRepository;

    @Autowired
    public SketchLikeService(SketchLikeRepository sketchLikeRepository) {
        this.sketchLikeRepository = sketchLikeRepository;
    }
}
