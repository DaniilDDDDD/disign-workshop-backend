package com.workshop.backgroundservice.service;

import com.workshop.backgroundservice.model.content.Sketch;
import com.workshop.backgroundservice.model.metadata.Comment;
import com.workshop.backgroundservice.model.metadata.Like;
import com.workshop.backgroundservice.model.metadata.Review;
import com.workshop.backgroundservice.repository.content.SketchRepository;
import com.workshop.backgroundservice.repository.metadata.CommentRepository;
import com.workshop.backgroundservice.repository.metadata.LikeRepository;
import com.workshop.backgroundservice.repository.metadata.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class MetadataService {

    private final SketchRepository sketchRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public MetadataService(SketchRepository sketchRepository, LikeRepository likeRepository, CommentRepository commentRepository, ReviewRepository reviewRepository) {
        this.sketchRepository = sketchRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
    }


    @Transactional
    public void removeNonValidLikesForPeriod(Date from, Date to) {

        List<Like> likes = likeRepository.findAllByDateBetween(from, to);

        List<String> validSketchesIds = sketchRepository
                .findAllByIdIn(
                        likes.stream().map(Like::getSketch).toList()
                ).stream()
                .map(Sketch::getId)
                .toList();

        List<Like> notValidLikes = likes
                .stream()
                .filter(like -> !validSketchesIds.contains(like.getSketch()))
                .toList();

        likeRepository.deleteAll(notValidLikes);
    }

    @Transactional
    public void removeNonValidCommentsForPeriod(Date from, Date to) {

        List<Comment> comments = commentRepository.findAllByDateBetween(from, to);

        List<String> validSketchesIds = sketchRepository
                .findAllByIdIn(
                        comments.stream().map(Comment::getSketch).toList()
                ).stream()
                .map(Sketch::getId)
                .toList();

        List<Comment> notValidComments = comments
                .stream()
                .filter(comment -> !validSketchesIds.contains(comment.getSketch()))
                .toList();

        commentRepository.deleteAll(notValidComments);
    }

    @Transactional
    public void removeNonValidReviewsForPeriod(Date from, Date to) {

        List<Review> reviews = reviewRepository.findAllByDateBetween(from, to);

        List<String> validSketchesIds = sketchRepository
                .findAllByIdIn(
                        reviews.stream().map(Review::getSketch).toList()
                ).stream()
                .map(Sketch::getId)
                .toList();

        List<Review> notValidReviews = reviews
                .stream()
                .filter(review -> !validSketchesIds.contains(review.getSketch()))
                .toList();

        reviewRepository.deleteAll(notValidReviews);
    }
}
