package com.workshop.metadataservice.repository.metadata.like;

import com.workshop.metadataservice.dto.EntityCount;

import java.util.List;
import java.util.Set;

public interface LikeRepositoryCustom {

    List<EntityCount> countSketchesLikes(Set<String> sketches);

}
