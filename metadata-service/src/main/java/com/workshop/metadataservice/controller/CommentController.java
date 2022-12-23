package com.workshop.metadataservice.controller;


import com.workshop.metadataservice.dto.EntityCount;
import com.workshop.metadataservice.dto.comment.CommentCreate;
import com.workshop.metadataservice.dto.comment.CommentRetrieve;
import com.workshop.metadataservice.dto.comment.CommentUpdate;
import com.workshop.metadataservice.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
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


    @GetMapping("/count")
    @Operation(
            summary = "Count sketches' comments",
            description = "Returns amount of sketches' comments"
    )
    public ResponseEntity<List<EntityCount>> count(
            @PathParam(value = "sketch")
                    List<String> sketches
    ) {
        return ResponseEntity.ok(
                commentService.count(sketches)
                        .entrySet().stream()
                        .map(EntityCount::parseEntry)
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


    @PostMapping("/{sketch}")
    @Operation(
            summary = "Create comment",
            description = "Create comment under certain sketch"
    )
    public ResponseEntity<CommentRetrieve> create(
            @PathVariable(name = "sketch")
                    String sketch,
            @RequestBody
            @Valid
                    CommentCreate commentCreate,
            Authentication authentication
    ) {
        return new ResponseEntity<>(
                CommentRetrieve.parseComment(
                        commentService.create(
                                sketch,
                                authentication,
                                commentCreate
                        )
                ),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update comment",
            description = "Update comment by id"
    )
    public ResponseEntity<CommentRetrieve> update(
            @PathVariable(name = "id")
                    String id,
            @Valid
            @RequestBody
                    CommentUpdate commentUpdate,
            Authentication authentication
    ) throws EntityNotFoundException {
        return ResponseEntity.ok(
                CommentRetrieve.parseComment(
                        commentService.update(
                                id,
                                authentication,
                                commentUpdate
                        )
                )
        );
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete comment",
            description = "Delete comment by id"
    )
    public ResponseEntity<String> delete(
            @PathVariable(name = "id")
                    String id,
            Authentication authentication
    ) throws EntityNotFoundException {
        commentService.delete(id, authentication);
        return new ResponseEntity<>(
                "Comment with id " + id + " deleted!",
                HttpStatus.NO_CONTENT
        );
    }

}
