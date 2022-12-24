package com.workshop.metadataservice.dto.comment;

import com.workshop.metadataservice.document.metadata.Comment;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CommentRetrieve {
    private String id;
    private String sketch;
    private String user;
    private String text;
    private Date date;

    public static CommentRetrieve parseComment(Comment comment) {
        return CommentRetrieve.builder()
                .id(comment.getId())
                .sketch(comment.getSketch())
                .user(comment.getUser())
                .text(comment.getText())
                .date(comment.getDate())
                .build();
    }
}
