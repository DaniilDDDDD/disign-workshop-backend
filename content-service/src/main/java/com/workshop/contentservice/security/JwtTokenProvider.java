package com.workshop.contentservice.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access-token.secret}")
    private String secret;

    private Key key;

}
