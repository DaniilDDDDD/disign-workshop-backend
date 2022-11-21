package com.workshop.contentservice.repository;


import com.workshop.contentservice.document.Tag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

    Optional<Tag> findByName(String name);

    List<Tag> findAllByNameIn(List<String> names);

}
