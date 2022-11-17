package com.workshop.contentservice.controller;

import com.workshop.contentservice.dto.sketch.SketchRetrieve;
import com.workshop.contentservice.service.SketchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.Min;
import java.util.List;

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
            description = "Get all sketches by tags"
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
                    sketchService.findAllPublic(page, size, sort).stream().map(
                            SketchRetrieve::parseSketchPublic
                    ).toList()
            );

        if (tags == null) {
            if (name.size() == 1)
                return ResponseEntity.ok(
                        List.of(SketchRetrieve.parseSketchPublic(
                                sketchService.findByNamePublic(name.get(0))
                        ))
                );
            return ResponseEntity.ok(
                    sketchService.findAllPublicByNameContains(
                            name, page, size, sort
                    ).stream().map(
                            SketchRetrieve::parseSketchPublic
                    ).toList()
            );
        }
        else
            return ResponseEntity.ok(
                    sketchService.findAllPublicByTagContains(
                            tags, page, size, sort
                    ).stream().map(
                            SketchRetrieve::parseSketchPublic
                    ).toList()
            );
    }

}
