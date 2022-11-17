package com.workshop.contentservice.repository;


import com.workshop.contentservice.document.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

    List<Tag> findAllByNameIn(List<String> names);

}
