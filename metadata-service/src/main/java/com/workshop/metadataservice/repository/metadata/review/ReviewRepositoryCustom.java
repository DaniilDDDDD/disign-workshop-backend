package com.workshop.metadataservice.repository.metadata.review;

import com.workshop.metadataservice.dto.EntityCount;

import java.util.List;
import java.util.Set;

public interface ReviewRepositoryCustom {

    List<EntityCount> countAllBySketch(Set<String> sketches);

}
