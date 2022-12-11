package com.workshop.metadataservice.document;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("sketch_likes")
@Getter
@Setter
@Builder
public class Like {

    @Id
    private String id;

    private String sketch;

    private String user;

}
