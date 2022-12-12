package com.workshop.metadataservice.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentCreate {
    @NotNull
    @NotBlank
    private String text;
}
