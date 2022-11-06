package com.workshop.authservice.repository;

import com.workshop.authservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    boolean existsTokenByValue(String value);

    Optional<Token> getTokenById(Long id);
    Optional<Token> getTokenByValue(String value);
    List<Token> getAllByOwnerId(Long id);

}
