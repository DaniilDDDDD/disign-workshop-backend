package com.workshop.metadataservice.service;

import com.workshop.metadataservice.document.Review;
import com.workshop.metadataservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    public Map<String,Long> count(
            List<String> sketches
    ) {
        Map<String, Long> results = new HashMap<>();
        for (String sketch : sketches)
            results.put(
                    sketch,
                    reviewRepository.countAllBySketch(sketch)
            );
        return results;
    }


    public List<Review> list(String sketch) {
        return reviewRepository.findAllBySketch(sketch);
    }



}
