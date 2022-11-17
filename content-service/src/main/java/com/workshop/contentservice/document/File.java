package com.workshop.contentservice.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document("files")
@Getter
@Setter
@Builder
public class File implements Serializable {

    @Id
    private String id;

    @NotNull(message = "Filename must be provided!")
    private String filename;

    private String description;

    @DBRef
    private Sketch sketch;

}
