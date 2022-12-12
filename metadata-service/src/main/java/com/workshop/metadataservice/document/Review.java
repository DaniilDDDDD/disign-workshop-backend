package com.workshop.metadataservice.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document("sketch_reviews")
@Getter
@Setter
@Builder
public class Review {

    @Id
    private String id;

    private String sketch;

    private String user;

    private String text;

    private byte rating;

    private Date date;

    private List<String> files;

}
