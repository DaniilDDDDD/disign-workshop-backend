package com.workshop.contentservice.dto.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workshop.contentservice.document.Sketch;
import com.workshop.contentservice.document.Tag;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagRetrieve {

    private String id;
    private String name;
    private List<String> sketches;

    public static TagRetrieve parseTag(Tag tag) {
        return TagRetrieve.builder()
                .id(tag.getId())
                .name(tag.getName())
//                .sketches(tag.getSketches() != null ?
//                        tag.getSketches().stream().map(Sketch::getId).toList() : null)
                .build();
    }

}
