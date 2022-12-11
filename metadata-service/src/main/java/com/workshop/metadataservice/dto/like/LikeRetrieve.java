package com.workshop.metadataservice.dto.like;

import com.workshop.metadataservice.document.Like;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LikeRetrieve {

    private String sketch;

    private String user;

    public static LikeRetrieve parseSketchLike(Like like) {
        return LikeRetrieve.builder()
                .sketch(like.getSketch())
                .user(like.getUser())
                .build();
    }

}
