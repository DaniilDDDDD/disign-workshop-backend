package com.workshop.contentservice.dto.tag;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TagCreate {

    @NotNull
    private String name;
}
