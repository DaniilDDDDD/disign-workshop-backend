package com.workshop.metadataservice.repository.metadata;

import com.workshop.metadataservice.document.metadata.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findAllBySketch(String sketch);

    List<Comment> findAllByUser(String user);

    Long countAllBySketch(String sketch);

}
