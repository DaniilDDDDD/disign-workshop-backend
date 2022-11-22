package com.workshop.contentservice.dto.sketch;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class SketchCreate {

    private String access;

    private List<String> tags;

    @NotNull(message = "Name must be provided!")
    private String name;

    private String description;

    private List<MultipartFile> files;

}
