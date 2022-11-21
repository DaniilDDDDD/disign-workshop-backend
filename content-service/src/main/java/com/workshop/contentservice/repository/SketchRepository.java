package com.workshop.contentservice.repository;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SketchRepository extends MongoRepository<Sketch, String> {

    List<Sketch> findAllByAccess(Access access, Pageable pageable);

    List<Sketch> findAllByAuthorEmail(String authorEmail, Pageable pageable);

    Optional<Sketch> findByName(String name);

    Optional<Sketch> findByNameAndAccess(String name, Access access);

    @Query("{'tags' : {$all: ?0}, 'access' : ?1}")
    List<Sketch> findAllByTagsAndAccess(List<Tag> tags, Access access, Pageable pageable);

    @Query("{'name' : {$text :  {$search : ?0 }}}")
    List<Sketch> findAllByNameAndAccess(List<String> names, Access access, Pageable pageable);
}
