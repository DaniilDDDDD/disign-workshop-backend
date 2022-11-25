package com.workshop.metadataservice.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sketch/like")
@Tag(name = "Sketches' likes", description = "Sketches' metadata endpoints")
public class SketchLikeController {
}
