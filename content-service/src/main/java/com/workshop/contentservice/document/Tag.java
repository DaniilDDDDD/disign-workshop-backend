package com.workshop.contentservice.document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Document("tag")
@Getter
@Setter
@Builder
public class Tag implements Serializable {

    @Id
    private String id;

    @NotNull(message = "Name must be provided!")
    @Indexed(unique = true)
    private String name;

}
