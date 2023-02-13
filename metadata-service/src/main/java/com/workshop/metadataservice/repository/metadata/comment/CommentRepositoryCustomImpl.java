package com.workshop.metadataservice.repository.metadata.comment;

import com.workshop.metadataservice.dto.EntityCount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public CommentRepositoryCustomImpl(
            @Qualifier("metadataMongoTemplate") MongoTemplate mongoTemplate
    ) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<EntityCount> countSketchesComments(Set<String> sketches) {

        GroupOperation groupBySketch = Aggregation
                .group("sketch").count().as("amount");

        MatchOperation sketchesInSetCriteria = Aggregation
                .match(Criteria.where("sketch").in(sketches));

        Aggregation aggregation = Aggregation
                .newAggregation(sketchesInSetCriteria, groupBySketch);

        AggregationResults<EntityCount> results = mongoTemplate
                .aggregate(aggregation, "comment", EntityCount.class);

        return results.getMappedResults();

    }
}
