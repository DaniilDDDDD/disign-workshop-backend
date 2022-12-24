package com.workshop.metadataservice.document.content;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Document("sketch")
@Getter
@Setter
@Builder
public class Sketch implements Serializable {

    @MongoId
    private String id;

}
