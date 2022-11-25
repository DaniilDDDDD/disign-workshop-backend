package com.workshop.metadataservice.repository;

import com.workshop.metadataservice.document.SketchComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SketchCommentRepository extends MongoRepository<SketchComment, String> {

    List<SketchComment> findAllBySketch(String sketch);

    List<SketchComment> findAllByUser(String user);

    Long countAllBySketch(String sketch);

}
