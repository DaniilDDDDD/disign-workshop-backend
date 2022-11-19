package com.workshop.contentservice.security;

import com.mongodb.client.FindIterable;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access-token.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public Authentication getAuthentication(String token) throws JwtException {
        Map<String, Object> credentials = getCredentials(token);
        return new JwtAuthentication(
                (String) credentials.get("email"),
                credentials,
                (Collection<? extends GrantedAuthority>) credentials.get("roles")
        );
    }


    public Map<String, Object> getCredentials(String token) throws  JwtException {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token);

        Map<String, Object> credentials = new HashMap<>();
        credentials.put("email", claims.getBody().getSubject());
        credentials.put("username", claims.getBody().get("username"));
        credentials.put("roles", claims.getBody().get("roles"));
        return credentials;
    }


    public String resolveToken(HttpServletRequest request) {
        String bearer_token = request.getHeader("Authorization");
        if (bearer_token != null && bearer_token.startsWith("Bearer_")) {
            return bearer_token.substring(7);
        }
        return null;
    }


    public boolean validateToken(String token) throws JwtException {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key).build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date()) &&
                    claims.getBody().containsKey("username") &&
                    // check subject is valid email
                    Pattern.compile(
                            "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                                    + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
                    ).matcher(
                            claims.getBody().getSubject()
                    ).matches();

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Jwt token is expired or invalid!");
        }
    }

}
