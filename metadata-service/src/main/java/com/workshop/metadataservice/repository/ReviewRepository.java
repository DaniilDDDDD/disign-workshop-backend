package com.workshop.metadataservice.repository;

import com.workshop.metadataservice.document.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findAllBySketch(String sketch);
    List<Review> findAllBySketchAndRating(String sketch, byte review);
    List<Review> findAllByUser(String user);

    Long countAllBySketch(String sketch);

}
