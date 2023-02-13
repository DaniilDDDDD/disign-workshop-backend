package com.workshop.metadataservice.repository.metadata.comment;

import com.workshop.metadataservice.dto.EntityCount;

import java.util.List;
import java.util.Set;

public interface CommentRepositoryCustom {

    List<EntityCount> countSketchesComments(Set<String> sketches);

}
