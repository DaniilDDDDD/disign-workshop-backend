package com.workshop.backgroundservice.repository.metadata;

import com.workshop.backgroundservice.model.metadata.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LikeRepository extends MongoRepository<Like, String> {

    List<Like> findAllByDateBetween(Date from, Date to);

}
