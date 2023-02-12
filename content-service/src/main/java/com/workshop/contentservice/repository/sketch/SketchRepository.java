package com.workshop.contentservice.repository.sketch;

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
public interface SketchRepository extends MongoRepository<Sketch, String>, SketchRepositoryCustom {

    List<Sketch> findAllByAccess(Access access, Pageable pageable);

    List<Sketch> findAllByAuthorEmail(String authorEmail, Pageable pageable);

    Optional<Sketch> findByName(String name);

    Optional<Sketch> findByNameAndAccess(String name, Access access);

}
