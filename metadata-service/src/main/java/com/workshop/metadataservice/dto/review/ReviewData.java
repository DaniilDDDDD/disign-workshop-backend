package com.workshop.metadataservice.dto.review;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ReviewData {
    private String text;
    private byte rating;
    private List<MultipartFile> files;
}
