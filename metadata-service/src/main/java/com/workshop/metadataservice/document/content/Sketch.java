package com.workshop.metadataservice.document.content;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;

@Document("sketch")
@Getter
@Setter
@Builder
public class Sketch implements Serializable {

    @Id
    private String id;

}
