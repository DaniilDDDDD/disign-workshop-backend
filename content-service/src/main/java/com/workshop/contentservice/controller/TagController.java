package com.workshop.contentservice.controller;


import com.workshop.contentservice.dto.tag.TagCreate;
import com.workshop.contentservice.dto.tag.TagRetrieve;
import com.workshop.contentservice.dto.tag.TagUpdate;
import com.workshop.contentservice.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/tags")
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
    public ResponseEntity<List<TagRetrieve>> list() {
        return ResponseEntity.ok(
                tagService.findAll().stream().map(TagRetrieve::parseTag).toList()
        );
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve tags",
            description = "Get tag by id"
    )
    public ResponseEntity<TagRetrieve> retrieve(
            @PathVariable(name = "id")
                    String id
    ) {
        return ResponseEntity.ok(
                TagRetrieve.parseTag(
                        tagService.getById(id)
                )
        );
    }


    @PostMapping("")
    @Operation(
            summary = "Create tag",
            description = "Create tags"
    )
    public ResponseEntity<TagRetrieve> create(
            @Valid
            @RequestBody
                    TagCreate tagCreate
    ) throws EntityExistsException {
        return new ResponseEntity<>(
                TagRetrieve.parseTag(tagService.create(
                        tagCreate
                )),
                HttpStatus.CREATED
        );
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update tag",
            description = "Update tag with provided id"
    )
    public ResponseEntity<TagRetrieve> update(
            @PathVariable(name = "id")
                    String id,
            @Valid
            @RequestBody
                    TagUpdate tagUpdate
    ) throws EntityNotFoundException {
        return new ResponseEntity<>(
                TagRetrieve.parseTag(
                        tagService.update(
                                id, tagUpdate
                        )
                ),
                HttpStatus.OK
        );
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete tag",
            description = "Delete tag with provided id"
    )
    public ResponseEntity<String> delete(
            @PathVariable(name = "id")
            String id
    ) throws EntityNotFoundException {
        tagService.delete(id);
        return new ResponseEntity<>(
                "Tag with id " + id + " deleted!",
                HttpStatus.NO_CONTENT
        );
    }

}
