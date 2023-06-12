package com.workshop.contentservice.dto.sketch;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SketchRetrieve implements Serializable {

    private String id;
    private String authorEmail;
    private String authorName;
    private String access;
    private List<Tag> tags;
    private Date publicationDate;
    private String name;
    private String description;
    private List<String> files;

    public static SketchRetrieve parseSketchPublic(Sketch sketch) {
        return SketchRetrieve.builder()
                .id(sketch.getId())
                .authorName(sketch.getAuthorName())
                .tags(sketch.getTags())
                .publicationDate(sketch.getPublicationDate())
                .name(sketch.getName())
                .description(sketch.getDescription())
                .files(sketch.getFiles())
                .build();
    }

    public static SketchRetrieve parseSketchPrivate(Sketch sketch) {
        return SketchRetrieve.builder()
                .id(sketch.getId())
                .authorEmail(sketch.getAuthorEmail())
                .authorName(sketch.getAuthorName())
                .access(sketch.getAccess().getName())
                .tags(sketch.getTags())
                .publicationDate(sketch.getPublicationDate())
                .name(sketch.getName())
                .description(sketch.getDescription())
                .files(sketch.getFiles())
                .build();
    }

}
