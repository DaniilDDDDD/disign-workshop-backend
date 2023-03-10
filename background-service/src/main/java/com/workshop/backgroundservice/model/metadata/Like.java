package com.workshop.backgroundservice.model.metadata;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;

@Document("like")
@Getter
@Setter
@Builder
@CompoundIndex(def = "{'sketch': 1, 'user': 1}", unique = true)
public class Like {

    @Id
    private String id;

    private String sketch;

    private String user;

    private Date date;

}
