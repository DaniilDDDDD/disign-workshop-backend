package com.workshop.metadataservice.controller;


import com.workshop.metadataservice.dto.EntityCount;
import com.workshop.metadataservice.dto.review.ReviewData;
import com.workshop.metadataservice.dto.review.ReviewRetrieve;
import com.workshop.metadataservice.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.websocket.server.PathParam;
import java.io.IOException;
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
    public ResponseEntity<List<EntityCount>> count(
            @PathParam(value = "sketch")
                    List<String> sketches
    ) {
        return ResponseEntity.ok(
                reviewService.count(sketches)
                        .entrySet().stream()
                        .map(EntityCount::parseEntry)
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


    @PostMapping("/{sketch}")
    @Operation(
            summary = "Create review",
            description = "Create review on a certain sketch"
    )
    public ResponseEntity<ReviewRetrieve> create(
            @PathVariable(name = "sketch")
                    String sketch,
            @ModelAttribute
                    ReviewData reviewData,
            Authentication authentication
    ) throws EntityExistsException, IllegalArgumentException, IOException {
        return new ResponseEntity<>(
                ReviewRetrieve.parseReview(
                        reviewService.create(
                                sketch,
                                reviewData,
                                authentication
                        )
                ),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{sketch}")
    @Operation(
            summary = "Update review",
            description = "Update review on a certain sketch"
    )
    public ResponseEntity<ReviewRetrieve> update(
            @PathVariable(name = "sketch")
                    String sketch,
            @ModelAttribute
                    ReviewData reviewData,
            Authentication authentication
    ) throws EntityExistsException, IllegalArgumentException, IOException {
        return ResponseEntity.ok().body(
                ReviewRetrieve.parseReview(
                        reviewService.update(
                                sketch,
                                reviewData,
                                authentication
                        )));
    }


    @DeleteMapping("/{sketch}")
    @Operation(
            summary = "Delete review",
            description = "Delete review on a certain sketch"
    )
    public ResponseEntity<String> update(
            @PathVariable(name = "sketch")
                    String sketch,
            Authentication authentication
    ) throws EntityNotFoundException {
        reviewService.delete(sketch, authentication);
        return new ResponseEntity<>(
                "Review on sketch with id " + sketch + " deleted!",
                HttpStatus.NO_CONTENT
        );
    }
}
