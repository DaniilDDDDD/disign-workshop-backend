package com.workshop.backgroundservice.repository.authentication;


import com.workshop.backgroundservice.model.user.InitializationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InitializationTokenRepository extends JpaRepository<InitializationToken, Long> {

    Optional<InitializationToken> findByValue(String value);

}
