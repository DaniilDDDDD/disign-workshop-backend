package com.workshop.metadataservice.repository;

import com.workshop.metadataservice.document.SketchLikes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SketchLikeRepository extends MongoRepository<SketchLikes, String> {

    List<SketchLikes> findAllBySketch(String sketch);
    List<SketchLikes> findAllByUser(String user);

    Long countAllBySketch(String sketch);

}
