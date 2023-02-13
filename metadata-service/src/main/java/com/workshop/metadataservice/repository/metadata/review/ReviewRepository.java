package com.workshop.metadataservice.repository.metadata.review;

import com.workshop.metadataservice.document.metadata.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface ReviewRepository extends MongoRepository<Review, String>, ReviewRepositoryCustom {

    boolean existsBySketchAndUser(String sketch, String user);

    List<Review> findAllBySketchInAndUser(Set<String> sketches, String user);

    List<Review> findAllBySketch(String sketch);
    Optional<Review> findBySketchAndUser(String sketch, String user);

}
