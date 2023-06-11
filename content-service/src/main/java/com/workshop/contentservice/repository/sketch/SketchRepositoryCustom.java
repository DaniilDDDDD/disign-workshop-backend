package com.workshop.contentservice.repository.sketch;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SketchRepositoryCustom {

    Page<Sketch> findAllByTagsAndAccess(List<Tag> tags, Access access, Pageable pageable);

    Page<Sketch> findAllByNameAndAccess(List<String> names, Access access, Pageable pageable);

    Page<Sketch> findAllByTagsAndName(List<Tag> tags, List<String> name, Access access, Pageable pageable);

}
