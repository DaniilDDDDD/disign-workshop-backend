package com.workshop.backgroundservice.repository.metadata;

import com.workshop.backgroundservice.model.metadata.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findAllByDateBetween(Date from, Date to);

}
