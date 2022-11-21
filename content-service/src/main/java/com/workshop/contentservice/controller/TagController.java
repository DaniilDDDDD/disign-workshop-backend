package com.workshop.contentservice.controller;


import com.workshop.contentservice.dto.tag.TagCreate;
import com.workshop.contentservice.dto.tag.TagRetrieve;
import com.workshop.contentservice.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sketches")
@Tag(name = "Tag", description = "Tags' endpoints")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }


    @GetMapping("")
    @Operation(
            summary = "List tags",
            description = "Get all tags"
    )
    public ResponseEntity<List<TagRetrieve>> list () {
        return ResponseEntity.ok(
                tagService.findAll().stream().map(TagRetrieve::parseTag).toList()
        );
    }


    @PostMapping("")
    @Operation(
            summary = "Create tag",
            description = "Create tags"
    )
    public ResponseEntity<List<TagRetrieve>> create(
            @Valid
            @RequestBody
                    TagCreate tagCreate
    ) throws EntityExistsException {
        return new ResponseEntity<>(
                tagService.create(
                        tagCreate
                ),
                HttpStatus.CREATED
        );
    }

}
