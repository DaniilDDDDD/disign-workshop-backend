package com.workshop.metadataservice.document.metadata;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Document("like")
@Getter
@Setter
@Builder
@CompoundIndex(def = "{'sketch': 1, 'user': 1}", unique = true)
public class Like implements Serializable {

    @Id
    private String id;

    private String sketch;

    private String user;

    private Date date;

}
