package com.workshop.authservice.dto.error;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NoFieldExceptionResponse {

    private String message;

}
