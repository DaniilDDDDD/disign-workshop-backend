package com.workshop.contentservice.repository.sketch;

import com.workshop.contentservice.document.Access;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SketchRepositoryImpl implements SketchRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SketchRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Sketch> findAllByTagsAndAccess(List<Tag> tags, Access access, Pageable pageable) {

        Query query = new Query();

        Criteria accessCriteria = Criteria.where("access").is(access);
        Criteria tagsCriteria = Criteria.where("tags").all(tags);

        query.addCriteria(accessCriteria);
        query.addCriteria(tagsCriteria);
        query.with(pageable);

        return mongoTemplate.find(query, Sketch.class);
    }

    @Override
    public List<Sketch> findAllByNameAndAccess(List<String> names, Access access, Pageable pageable) {
        Query query = new Query();

        Criteria accessCriteria = Criteria.where("access").is(access);
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingAny(names.toArray(new String[0]));

        query.addCriteria(textCriteria);
        query.addCriteria(accessCriteria);
        query.with(pageable);

        return mongoTemplate.find(query, Sketch.class);
    }

    @Override
    public List<Sketch> findAllByTagsAndName(List<Tag> tags, List<String> name, Access access, Pageable pageable) {
        Query query = new Query();

        Criteria accessCriteria = Criteria.where("access").is(access);
        Criteria tagsCriteria = Criteria.where("tags").all(tags);
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingAny(name.toArray(new String[0]));

        query.addCriteria(accessCriteria);
        query.addCriteria(tagsCriteria);
        query.addCriteria(textCriteria);
        query.with(pageable);

        return mongoTemplate.find(query, Sketch.class);
    }

}
