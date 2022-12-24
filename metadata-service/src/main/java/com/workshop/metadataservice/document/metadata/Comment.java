package com.workshop.metadataservice.document.metadata;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Document("sketch_comment")
@Getter
@Setter
@Builder
public class Comment {

    @MongoId
    private String id;

    private String sketch;

    private String user;

    private String text;

    private Date date;

    private List<String> files;
}
