package com.workshop.contentservice.repository;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SketchRepository extends MongoRepository<Sketch, String> {

    Page<Sketch> findAllByAccess(Access access, Pageable pageable);
    Page<Sketch> findAllByAuthorAndAccess(Long author, Access access, Pageable pageable);

    Optional<Sketch> findAllByNameAndAccess(String name, Access access);
    Page<Sketch> findAllByNameContainsAndAccess(List<String> name, Access access, Pageable pageable);

    Page<Sketch> findAllByTagsContainsAndAccess(List<Tag> tags, Access access, Pageable pageable);

}
