package com.workshop.metadataservice.repository.metadata;

import com.workshop.metadataservice.document.metadata.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    boolean existsBySketchAndUser(String sketch, String user);

    List<Review> findAllBySketch(String sketch);
    Optional<Review> findBySketchAndUser(String sketch, String user);

    Long countAllBySketch(String sketch);

}
