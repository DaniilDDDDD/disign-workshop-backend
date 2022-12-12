package com.workshop.metadataservice.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCount {
    private String sketch;
    private Long amount;
}
