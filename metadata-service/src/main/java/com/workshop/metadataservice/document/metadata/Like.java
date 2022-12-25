package com.workshop.metadataservice.document.metadata;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document("like")
@Getter
@Setter
@Builder
@CompoundIndex(def = "{'sketch': 1, 'number': 1}", unique = true)
public class Like {

    @MongoId
    private String id;

    private String sketch;

    private String user;

    private Date date;

}
