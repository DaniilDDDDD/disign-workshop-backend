package com.workshop.metadataservice.dto.like;

import com.workshop.metadataservice.document.metadata.Like;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class LikeRetrieve implements Serializable {

    private String sketch;

    private String user;

    private Date date;

    public static LikeRetrieve parseLike(Like like) {
        return LikeRetrieve.builder()
                .sketch(like.getSketch())
                .user(like.getUser())
                .date(like.getDate())
                .build();
    }

}
