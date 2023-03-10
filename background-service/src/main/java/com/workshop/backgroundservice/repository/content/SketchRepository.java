package com.workshop.backgroundservice.repository.content;

import com.workshop.backgroundservice.model.content.Sketch;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SketchRepository extends MongoRepository<Sketch, String> {

    List<Sketch> findAllByIdIn(List<String> ids);

}
