package com.workshop.contentservice.controller;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.dto.sketch.SketchRetrieve;
import com.workshop.contentservice.service.SketchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("sketch/")
@Tag(name = "User", description = "Sketches' endpoints")
public class SketchController {

    private final SketchService sketchService;

    @Autowired
    public SketchController(SketchService sketchService) {
        this.sketchService = sketchService;
    }

    @GetMapping("")
    @Operation(
            summary = "List sketches",
            description = "Get all sketches by tags or name"
    )
    public ResponseEntity<List<SketchRetrieve>> list(
            @Min(value = 0)
            @RequestParam(value = "page", defaultValue = "0")
            int page,
            @Min(value = 1)
            @RequestParam(value = "size", defaultValue = "9")
            int size,
            @RequestParam(value = "sort", defaultValue = "publicationDate")
            String sort,
            @RequestParam(value = "tags", required = false)
            List<String> tags,
            @RequestParam(value = "name", required = false) // lookup by name is executed by every word
            List<String> name
    ) throws EntityNotFoundException {

        if (tags == null && name == null)
            return ResponseEntity.ok(
                    sketchService.findAllPublic(
                            page, size, sort
                    ).stream().map(
                            SketchRetrieve::parseSketchPublic
                    ).toList()
            );

        if (tags == null) {
            return ResponseEntity.ok(
                    sketchService.findAllPublicByNameContains(
                            name, page, size, sort
                    ).stream().map(
                            SketchRetrieve::parseSketchPublic
                    ).toList()
            );
        } else
            return ResponseEntity.ok(
                    sketchService.findAllPublicByTagContains(
                            tags, page, size, sort
                    ).stream().map(
                            SketchRetrieve::parseSketchPublic
                    ).toList()
            );
    }

    @GetMapping("sketch/{id}")
    @Operation(
            summary = "Retrieve sketch",
            description = "Get sketch by id"
    )
    public ResponseEntity<SketchRetrieve> retrieve(
            @PathVariable(name = "id")
            String id,
            Authentication authentication
    ) throws EntityNotFoundException {
        Sketch sketch = sketchService.findPublicById(id);

        if (sketch.getAuthor() == authentication.getPrincipal())
            return ResponseEntity.ok(
                    SketchRetrieve.parseSketchPrivate(sketch)
            );
        if (sketch.getAccess() == Access.PUBLIC)
            return ResponseEntity.ok(
                    SketchRetrieve.parseSketchPublic(sketch));
        throw new EntityNotFoundException("Sketch with provided id not found!");
    }

    @GetMapping("sketch/me")
    @Operation(
            summary = "List principal's sketches",
            description = "List all sketches of request sender"
    )
    public ResponseEntity<SketchRetrieve> me (
            Authentication authentication
    ) {

    }
}
