package com.workshop.metadataservice.dto.review;

import com.workshop.metadataservice.document.metadata.Review;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class ReviewRetrieve implements Serializable {
    private String id;
    private String sketch;
    private String user;
    private String text;
    private byte rating;
    private List<String> files;
    private Date date;

    public static ReviewRetrieve parseReview(Review review) {
        return ReviewRetrieve.builder()
                .id(review.getId())
                .sketch(review.getSketch())
                .user(review.getUser())
                .rating(review.getRating())
                .text(review.getText())
                .date(review.getDate())
                .files(review.getFiles())
                .build();
    }
}
