package com.workshop.metadataservice.repository;

import com.workshop.metadataservice.document.SketchReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SketchReviewRepository extends MongoRepository<SketchReview, String> {

    List<SketchReview> findAllBySketch(String sketch);
    List<SketchReview> findAllBySketchAndRating(String sketch, byte review);
    List<SketchReview> findAllByUser(String user);

    Long countAllBySketch(String sketch);

}
