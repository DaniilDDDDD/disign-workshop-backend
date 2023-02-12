package com.workshop.metadataservice.service;


import com.workshop.metadataservice.document.metadata.Like;
import com.workshop.metadataservice.repository.content.SketchRepository;
import com.workshop.metadataservice.repository.metadata.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final SketchRepository sketchRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository,
            SketchRepository sketchRepository
    ) {
        this.likeRepository = likeRepository;
        this.sketchRepository = sketchRepository;
    }


    public Set<Like> retrieve(Set<String> sketches) {
        return likeRepository.findAllBySketchIn(sketches);
    }


    public Map<String, Long> count(Set<String> sketches) {
        return sketches.stream().collect(
                Collectors.toMap(
                        e -> e,
                        likeRepository::countAllBySketch
                ));
    }


    public Set<Like> create(
            Set<String> sketches,
            Authentication authentication
    ) {
        String userEmail = (String) authentication.getPrincipal();

        return sketches.stream()
                .map(
                        sketch -> {
                            if (!sketchRepository.existsById(sketch))
                                return null;

                            Optional<Like> likeData = likeRepository.findBySketchAndUser(sketch, userEmail);
                            if (likeData.isEmpty())
                                return likeRepository.save(
                                        Like.builder()
                                                .sketch(sketch)
                                                .user(userEmail)
                                                .date(new Date())
                                                .build()
                                );
                            return likeData.get();
                        }
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

    }


    public void delete(
            Set<String> sketches, Authentication authentication
    ) {
        likeRepository.deleteAllBySketchInAndUser(sketches, (String) authentication.getPrincipal());
    }

}
