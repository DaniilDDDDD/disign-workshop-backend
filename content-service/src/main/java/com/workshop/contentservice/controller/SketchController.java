package com.workshop.contentservice.controller;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.dto.sketch.SketchCreate;
import com.workshop.contentservice.dto.sketch.SketchRetrieve;
import com.workshop.contentservice.dto.sketch.SketchUpdate;
import com.workshop.contentservice.service.SketchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/sketches")
@Tag(name = "Sketch", description = "Sketches' endpoints")
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
            @RequestParam(value = "name", required = false)
            List<String> name
    ) throws EntityNotFoundException {

        if (tags == null)
            tags = List.of();

        if (name == null)
            name = List.of();

        return ResponseEntity.ok(
                sketchService.findAllPublicByTagsAndName(
                        tags, name, page, size, sort
                    ).stream().map(
                            SketchRetrieve::parseSketchPublic
                    ).toList()
        );
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve sketch",
            description = "Get sketch by id"
    )
    public ResponseEntity<SketchRetrieve> retrieve(
            @PathVariable(name = "id")
            String id,
            Authentication authentication
    ) throws EntityNotFoundException {
        Sketch sketch = sketchService.findById(id);

        if (Objects.equals(sketch.getAuthorEmail(), authentication.getPrincipal())) {
            return ResponseEntity.ok(
                    SketchRetrieve.parseSketchPrivate(sketch)
            );
        }
        if (sketch.getAccess() == Access.PUBLIC)
            return ResponseEntity.ok(
                    SketchRetrieve.parseSketchPublic(sketch)
            );

        throw new EntityNotFoundException("Sketch with provided id not found!");
    }


    @GetMapping("/me")
    @Operation(
            summary = "List principal's sketches",
            description = "List all sketches of request sender"
    )
    public ResponseEntity<List<SketchRetrieve>> me(
            @Min(value = 0)
            @RequestParam(value = "page", defaultValue = "0")
            int page,
            @Min(value = 1)
            @RequestParam(value = "size", defaultValue = "9")
            int size,
            @RequestParam(value = "sort", defaultValue = "publicationDate")
            String sort,
            Authentication authentication
    ) {
        return ResponseEntity.ok(sketchService.findAllByAuthorEmail(
                        (String) authentication.getPrincipal(),
                        page, size, sort
                ).stream().map(
                        SketchRetrieve::parseSketchPrivate
                ).toList()
        );
    }


    @PostMapping(value = "", consumes = {"multipart/form-data"})
    @Operation(
            summary = "Create sketch",
            description = "Create sketch with author equal to request sender"
    )
    public ResponseEntity<SketchRetrieve> create(
            @Valid
            @ModelAttribute
            SketchCreate sketchCreate,
            Authentication authentication
    ) throws EntityExistsException, IllegalArgumentException, IOException {
        return new ResponseEntity<>(
                SketchRetrieve.parseSketchPrivate(
                        sketchService.create(sketchCreate, authentication)
                ),
                HttpStatus.CREATED
        );
    }


    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Operation(
            summary = "Update sketch",
            description = "Update sketch with provided data"
    )
    public ResponseEntity<SketchRetrieve> update(
            @PathVariable(name = "id")
            String id,
            @Valid
            @ModelAttribute
            SketchUpdate sketchUpdate,
            Authentication authentication
    ) throws EntityNotFoundException, IOException {
        return ResponseEntity.ok(
                SketchRetrieve.parseSketchPublic(
                        sketchService.update(
                                id,
                                sketchUpdate,
                                authentication
                        )
                )
        );
    }


    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete sketch",
            description = "Delete sketch with provided id"
    )
    public ResponseEntity<String> delete(
            @PathVariable
            String id,
            Authentication authentication
    ) throws EntityNotFoundException {
        sketchService.delete(id, authentication);
        return new ResponseEntity<>(
                "Sketch with id " + id + " is deleted!",
                HttpStatus.NO_CONTENT
        );
    }


    @GetMapping("/resource")
    @Operation(
            summary = "Get resource",
            description = "Get by link."
    )
    public ResponseEntity<Resource> avatar(
            @RequestParam("url") String url
    ) throws FileNotFoundException {
        Resource resource = sketchService.getResource(url);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
