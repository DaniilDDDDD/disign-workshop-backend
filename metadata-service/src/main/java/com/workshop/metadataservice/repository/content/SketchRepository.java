package com.workshop.metadataservice.repository.content;

import com.workshop.metadataservice.document.content.Sketch;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SketchRepository extends MongoRepository<Sketch, String> {

}
