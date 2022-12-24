package com.workshop.metadataservice.controller;

import com.workshop.metadataservice.dto.EntityCount;
import com.workshop.metadataservice.dto.like.LikeRetrieve;
import com.workshop.metadataservice.service.LikeService;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/like")
@Tag(name = "Likes", description = "Sketches' likes metadata endpoints")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }


    @GetMapping("/count")
    @Operation(
            summary = "Count sketches' likes",
            description = "Returns amount of sketches' likes"
    )
    public ResponseEntity<List<EntityCount>> count(
            @PathParam(value = "sketch")
                    Set<String> sketches
    ) {
        return ResponseEntity.ok(
                likeService.count(sketches)
                        .entrySet().stream()
                        .map(EntityCount::parseEntry)
                        .toList()
        );
    }


    @GetMapping("")
    @Operation(
            summary = "Retrieve likes",
            description = "Retrieve likes provided sketches"
    )
    public ResponseEntity<Set<LikeRetrieve>> retrieve(
            @RequestParam(value = "sketch")
                    Set<String> sketches
    ) {
        return ResponseEntity.ok(
                likeService.retrieve(sketches)
                        .stream()
                        .map(LikeRetrieve::parseLike)
                        .collect(Collectors.toSet())
        );
    }


    @PostMapping("")
    @Operation(
            summary = "Create likes",
            description = "Create likes on provided sketches"
    )
    public ResponseEntity<Set<LikeRetrieve>> create(
            @RequestParam("sketch")
                    Set<String> sketches,
            Authentication authentication
    ) throws EntityExistsException {
        return new ResponseEntity<>(
                likeService
                        .create(sketches,authentication)
                        .stream()
                        .map(LikeRetrieve::parseLike)
                        .collect(Collectors.toSet()),
                HttpStatus.CREATED
        );
    }


    @DeleteMapping("")
    @Operation(
            summary = "Delete likes",
            description = "Delete like of certain sketch"
    )
    public ResponseEntity<String> delete(
            @RequestParam("sketch")
                    Set<String> sketches,
            Authentication authentication
    ) {
        likeService.delete(
                sketches,
                authentication
        );
        return new ResponseEntity<>(
                "Likes on provided sketches deleted!",
                HttpStatus.NO_CONTENT
        );
    }

}
