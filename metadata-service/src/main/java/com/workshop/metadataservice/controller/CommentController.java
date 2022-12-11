package com.workshop.metadataservice.controller;


import com.workshop.metadataservice.dto.comment.CommentCount;
import com.workshop.metadataservice.dto.comment.CommentRetrieve;
import com.workshop.metadataservice.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Tag(name = "Comments", description = "Sketches' comments metadata endpoints")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/{sketch}/count")
    @Operation(
            summary = "Count sketch's comments",
            description = "Returns amount of certain sketch's comments"
    )
    public ResponseEntity<List<CommentCount>> count(
            @PathVariable(name = "sketch")
            List<String> sketches
    ) {
        return ResponseEntity.ok(
                commentService.count(sketches)
                        .stream()
                        .map(CommentRetrieve::parseComment)
                        .toList()
        );
    }


    @GetMapping("/{sketch}")
    @Operation(
            summary = "List sketch's comments",
            description = "List all comments of certain sketch"
    )
    public ResponseEntity<List<CommentRetrieve>> list(
            @PathVariable(name = "sketch")
            String sketch
    ) {
        return ResponseEntity.ok(
                commentService.list(sketch)
                        .stream()
                        .map(CommentRetrieve::parseComment)
                        .toList()
        );
    }
}
