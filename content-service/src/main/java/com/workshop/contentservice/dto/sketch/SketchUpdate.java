package com.workshop.contentservice.dto.sketch;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class SketchUpdate {

    private String access;
    private List<String> tags;
    private String name;
    private String description;
    private List<MultipartFile> files;

}
