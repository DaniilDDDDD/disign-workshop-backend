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
                    List<String> sketches
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
            summary = "Retrieve sketches' likes",
            description = "Retrieve likes of certain sketches"
    )
    public ResponseEntity<List<LikeRetrieve>> retrieve(
            @RequestParam(value = "sketch")
            List<String> sketches
    ) {
        return ResponseEntity.ok(
                likeService.retrieveSketchLikes(sketches)
                        .stream()
                        .map(LikeRetrieve::parseSketchLike)
                        .toList()
        );
    }


    @PostMapping("/{sketch}")
    @Operation(
            summary = "Create sketch like",
            description = "Create like of certain sketch"
    )
    public ResponseEntity<LikeRetrieve> create(
            @PathVariable(value = "sketch")
            String sketch,
            Authentication authentication
    ) throws EntityExistsException {
        return new ResponseEntity<>(
                LikeRetrieve.parseSketchLike(
                        likeService.create(
                                sketch,
                                (String) authentication.getPrincipal()
                        )
                ),
                HttpStatus.CREATED
        );
    }


    @DeleteMapping("/{sketch}")
    @Operation(
            summary = "Delete sketch like",
            description = "Delete like of certain sketch"
    )
    public ResponseEntity<String> delete(
            @PathVariable(value = "sketch")
            String sketch,
            Authentication authentication
    ) throws EntityNotFoundException {
        likeService.delete(
                sketch,
                authentication
        );
        return new ResponseEntity<>(
                "Sketch " + sketch + " deleted!",
                HttpStatus.NO_CONTENT
        );
    }

}
