package com.workshop.metadataservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class EntityCount {
    private String id;
    private Long amount;

    public static EntityCount parseEntry(Map.Entry<String, Long> entry) {
        return EntityCount.builder()
                .id(entry.getKey())
                .amount(entry.getValue())
                .build();
    }
}