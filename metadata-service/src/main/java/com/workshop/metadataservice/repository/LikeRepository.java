package com.workshop.metadataservice.repository;

import com.workshop.metadataservice.document.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LikeRepository extends MongoRepository<Like, String> {

    Optional<Like> findBySketchAndUser(String sketch, String user);
    List<Like> findAllBySketch(String sketch);
    List<Like> findAllBySketchIn(List<String> sketches);

    List<Like> findAllByUser(String user);

    boolean existsBySketchAndUser(String sketch, String user);

}
