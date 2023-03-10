package com.workshop.backgroundservice.repository.metadata;

import com.workshop.backgroundservice.model.metadata.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllByDateBetween(Date from, Date to);

}
