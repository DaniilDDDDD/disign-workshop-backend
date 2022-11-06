package com.workshop.authservice.service;

import com.workshop.authservice.model.Token;
import com.workshop.authservice.model.TokenType;
import com.workshop.authservice.model.User;
import com.workshop.authservice.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public boolean tokenExistByValue(String value) {
        return tokenRepository.existsTokenByValue(value);
    }

    public Token getTokenByValue(String value) throws EntityNotFoundException {
        Optional<Token> token = tokenRepository.getTokenByValue(value);
        if (token.isEmpty()) throw new EntityNotFoundException("Provided token does not exist!");
        return token.get();
    }

    public Token create(User owner, String value, Date expirationDate, TokenType type) throws EntityExistsException {

        if (tokenRepository.existsTokenByValue(value))
            throw new EntityExistsException("Token with provided value already exists!");

        return tokenRepository.save(
                Token.builder()
                        .type(type)
                        .owner(owner)
                        .value(value)
                        .expirationDate(expirationDate)
                        .build()
        );
    }

    public Token update(String value, Token token) {
        token.setValue(value);
        return tokenRepository.save(token);
    }


}
