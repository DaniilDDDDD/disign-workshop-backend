package com.workshop.backgroundservice.dto.error;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NoFieldException {

    private String message;

}
