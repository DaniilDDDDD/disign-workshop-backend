package com.workshop.contentservice.document;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import javax.persistence.Convert;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document("sketch")
@Getter
@Setter
@Builder
public class Sketch implements Serializable {

    @Id
    private String id;

    @NotNull(message = "Owner email must be provided!")
    private String authorEmail;

    @NotNull(message = "Owner name must be provided!")
    private String authorName;

    @NotNull(message = "Access must be provided!")
    @Convert(converter = Access.AccessConverter.class)
    private Access access;

    @DocumentReference
    private List<Tag> tags;

    @NotNull(message = "Publication date must not be null!")
    private Date publicationDate;

    @NotNull(message = "Name must be provided!")
    @Indexed(unique = true)
    @TextIndexed
    private String name;

    private String description;


    private List<String> files;

}
