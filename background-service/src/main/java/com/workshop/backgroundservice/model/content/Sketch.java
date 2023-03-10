package com.workshop.backgroundservice.model.content;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;


@Document("sketch")
@Getter
@Setter
@Builder
public class Sketch {

    @Id
    private String id;

}
