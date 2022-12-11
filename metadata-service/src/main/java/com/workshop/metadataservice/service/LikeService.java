package com.workshop.metadataservice.service;


import com.workshop.metadataservice.document.Like;
import com.workshop.metadataservice.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public List<Like> retrieveSketchLikes(List<String> sketches) {
        return likeRepository.findAllBySketchIn(sketches);
    }


    public Like create(
            String sketch,
            String user
    ) throws EntityExistsException {
        if (likeRepository.existsBySketchAndUser(sketch, user))
            throw new EntityExistsException("Like on provided sketch already exists!");
        return likeRepository.save(
                Like.builder()
                        .sketch(sketch)
                        .user(user)
                        .build()
        );
    }


    public void delete(
            String sketch, Authentication authentication
    ) throws EntityNotFoundException {

        Optional<Like> sketchLike = likeRepository.findBySketchAndUser(
                sketch,
                ((Map<String, String>) authentication.getCredentials()).get("email")
        );

        if (sketchLike.isEmpty())
            throw new EntityNotFoundException(
                    "Like on provided sketch does not exist!");

        likeRepository.delete(sketchLike.get());
    }

}
