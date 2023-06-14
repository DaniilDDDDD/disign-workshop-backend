package com.workshop.metadataservice.document.metadata;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document("comment")
@Getter
@Setter
@Builder
public class Comment implements Serializable {

    @Id
    private String id;

    private String sketch;

    private String user;

    private String text;

    private Date date;

    private List<String> files;
}
