package com.workshop.metadataservice.controller;


import com.workshop.metadataservice.dto.review.ReviewCount;
import com.workshop.metadataservice.dto.review.ReviewRetrieve;
import com.workshop.metadataservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/review")
@Tag(name = "Reviews", description = "Sketches' review metadata endpoints")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/count")
    @Operation(
            summary = "Count sketches' reviews",
            description = "Returns amount of sketches' reviews"
    )
    public ResponseEntity<List<ReviewCount>> count(
            @PathParam(value = "sketch")
                    List<String> sketches
    ) {
        return ResponseEntity.ok(
                reviewService.count(sketches)
                        .entrySet().stream()
                        .map(e -> ReviewCount.builder()
                                .sketch(e.getKey())
                                .amount(e.getValue())
                                .build())
                        .toList()
        );
    }


    @GetMapping("/{sketch}")
    @Operation(
            summary = "List sketches' reviews",
            description = "Returns all reviews of certain sketch"
    )
    public ResponseEntity<List<ReviewRetrieve>> list(
            @PathVariable(name = "sketch")
                    String sketch
    ) {
        return ResponseEntity.ok(
                reviewService.list(sketch)
                        .stream()
                        .map(ReviewRetrieve::parseReview)
                        .toList()
        );
    }
}
