package com.workshop.metadataservice.dto.error;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class NoFieldException implements Serializable {

    private String message;

}
