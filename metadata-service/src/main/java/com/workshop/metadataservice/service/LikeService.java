package com.workshop.metadataservice.service;


import com.workshop.metadataservice.document.metadata.Like;
import com.workshop.metadataservice.dto.EntityCount;
import com.workshop.metadataservice.repository.content.SketchRepository;
import com.workshop.metadataservice.repository.metadata.like.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@CacheConfig(cacheNames = "like")
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


    @Cacheable
    public Set<Like> retrieve(Set<String> sketches) {
        return likeRepository.findAllBySketchIn(sketches);
    }


    @Cacheable
    public List<EntityCount> count(Set<String> sketches) {
        return likeRepository.countSketchesLikes(sketches);
    }


    @CachePut
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


    @CacheEvict(key = "#sketches")
    public void delete(
            Set<String> sketches, Authentication authentication
    ) {
        likeRepository.deleteAllBySketchInAndUser(sketches, (String) authentication.getPrincipal());
    }

}
