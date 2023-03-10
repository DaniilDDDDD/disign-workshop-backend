package com.workshop.backgroundservice.model.metadata;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Document("comment")
@Getter
@Setter
@Builder
public class Comment {

    @Id
    private String id;

    private String sketch;

    private String user;

    private Date date;

}
