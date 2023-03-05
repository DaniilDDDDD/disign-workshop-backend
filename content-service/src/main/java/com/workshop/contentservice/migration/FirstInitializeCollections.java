package com.workshop.contentservice.migration;

import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "1_initialize_collections", order = "1", author = "DaniilDDDDD")
public class FirstInitializeCollections {

    private final MongoTemplate mongoTemplate;


    public FirstInitializeCollections(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Execution
    public void execute() {

        if (!mongoTemplate.collectionExists(Sketch.class))
            mongoTemplate.createCollection(Sketch.class);

        if (!mongoTemplate.collectionExists(Tag.class))
        mongoTemplate.createCollection(Tag.class);

    }

    @RollbackExecution
    public void rollbackExecution() {

        if (mongoTemplate.collectionExists(Sketch.class))
            mongoTemplate.dropCollection(Sketch.class);

        if (mongoTemplate.collectionExists(Tag.class))
        mongoTemplate.dropCollection(Tag.class);

    }

}
