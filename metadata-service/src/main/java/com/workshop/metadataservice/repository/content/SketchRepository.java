package com.workshop.metadataservice.repository.content;

import com.workshop.metadataservice.document.content.Sketch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SketchRepository extends MongoRepository<Sketch, String> {

}
