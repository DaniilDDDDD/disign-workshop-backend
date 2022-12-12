package com.workshop.metadataservice.dto.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CommentUpdate {
    @NotNull
    @NotBlank
    private String text;
}
