package com.workshop.authservice.repository;

import com.workshop.authservice.model.InitializationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InitializationTokenRepository extends JpaRepository<InitializationToken, Long> {

    void deleteAllByUserId(Long userId);

}
