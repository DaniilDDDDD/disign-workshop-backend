package com.workshop.metadataservice.service;

import com.workshop.metadataservice.document.metadata.Review;
import com.workshop.metadataservice.dto.review.ReviewData;
import com.workshop.metadataservice.repository.content.SketchRepository;
import com.workshop.metadataservice.repository.metadata.ReviewRepository;
import com.workshop.metadataservice.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReviewService {

    @Value("${filesRoot}")
    private String filesRoot;

    @Value("${maxFilesAmount}")
    private int maxFilesAmount;


    private final ReviewRepository reviewRepository;
    private final SketchRepository sketchRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, SketchRepository sketchRepository) {
        this.reviewRepository = reviewRepository;
        this.sketchRepository = sketchRepository;
    }


    public Map<String,Long> count(
            List<String> sketches
    ) {
        return sketches.stream().collect(
                Collectors.toMap(
                        e -> e,
                        reviewRepository::countAllBySketch
                ));
    }


    public List<Review> list(String sketch) {
        return reviewRepository.findAllBySketch(sketch);
    }


    public Review create(
            String sketch,
            ReviewData reviewData,
            Authentication authentication
    ) throws EntityExistsException, IllegalArgumentException, IOException {

        if (!sketchRepository.existsById(sketch))
            throw new EntityExistsException("Sketch with provided id does not exist!");

        String authorEmail = (String) authentication.getPrincipal();

        if (reviewRepository.existsBySketchAndUser(sketch, authorEmail))
            throw new EntityExistsException("Review on provided sketch already exists!");

        // TODO: check if sketch does not exit via message broker
        Review review = Review.builder()
                .sketch(sketch)
                .user((String) authentication.getPrincipal())
                .text(reviewData.getText())
                .rating(reviewData.getRating())
                .date(new Date())
                .build();

        if (reviewData.getFiles() != null) {

            if (reviewData.getFiles().size() > maxFilesAmount)
                throw new IllegalArgumentException("Amount of files must not be more then " + maxFilesAmount + "!");

            String uploadDir = this.filesRoot + review.getUser() + "/";
            List<String> files = new ArrayList<>();
            for (MultipartFile multipartFile : reviewData.getFiles()) {
                String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUtil.saveFile(
                        uploadDir,
                        filename,
                        multipartFile
                );
                files.add(uploadDir + filename);
            }
            review.setFiles(files);
        }

        return reviewRepository.save(review);
    }

    public Review update(
            String sketch,
            ReviewData reviewData,
            Authentication authentication
    ) throws EntityNotFoundException, IllegalArgumentException, IOException {

        String authorEmail = (String) authentication.getPrincipal();

        Optional<Review> reviewEntity = reviewRepository.findBySketchAndUser(sketch, authorEmail);
        if (reviewEntity.isEmpty()) throw new EntityNotFoundException("Review on provided sketch does not exist!");

        Review review = reviewEntity.get();

        if (!Objects.equals(review.getUser(), authorEmail))
            throw new AccessDeniedException("Access denied!");


        review.setText(reviewData.getText() != null ? reviewData.getText() : review.getText());
        review.setText(reviewData.getRating() != 0 ? reviewData.getText() : review.getText());

        if (reviewData.getFiles() != null) {

            if (reviewData.getFiles().size() > maxFilesAmount)
                throw new IllegalArgumentException("Amount of files must not be more then " + maxFilesAmount + "!");

            for (String filename : review.getFiles())
                FileUtil.deleteFile(filename);

            String uploadDir = this.filesRoot + review.getUser() + "/";
            List<String> files = new ArrayList<>();
            for (MultipartFile multipartFile : reviewData.getFiles()) {
                String filename = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUtil.saveFile(
                        uploadDir,
                        filename,
                        multipartFile
                );
                files.add(uploadDir + filename);
            }
            review.setFiles(files);
        }

        return reviewRepository.save(review);
    }


    public void delete(
            String sketch,
            Authentication authentication
    ) throws EntityNotFoundException {

        String userEmail = (String) authentication.getPrincipal();

        Optional<Review> reviewData = reviewRepository.findBySketchAndUser(
                sketch, userEmail);
        if (reviewData.isEmpty()) throw new EntityNotFoundException("Review on provided sketch does not exist!");

        Review review = reviewData.get();

        if (!Objects.equals(review.getUser(), userEmail))
            throw new AccessDeniedException("Access denied!");

        reviewRepository.delete(review);
    }
}
