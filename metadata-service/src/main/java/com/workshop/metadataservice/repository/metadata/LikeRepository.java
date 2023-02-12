package com.workshop.metadataservice.repository.metadata;

import com.workshop.metadataservice.document.metadata.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface LikeRepository extends MongoRepository<Like, String> {

    Long countAllBySketch(String sketch);

    Optional<Like> findBySketchAndUser(String sketch, String user);
    Set<Like> findAllBySketchIn(Set<String> sketches);

    void deleteAllBySketchInAndUser(Set<String> sketches, String user);

}
