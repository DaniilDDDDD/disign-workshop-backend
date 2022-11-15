package com.workshop.authservice.security;

import com.workshop.authservice.model.Role;
import com.workshop.authservice.model.Token;
import com.workshop.authservice.model.TokenType;
import com.workshop.authservice.model.User;
import com.workshop.authservice.service.TokenService;
import com.workshop.authservice.service.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access-token.secret}")
    private String secret;

    private Key key;

    @Value("${jwt.access-token.expired}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expired}")
    private long refreshTokenValidityInMilliseconds;

    private final UserService userService;

    private final TokenService tokenService;

    @Autowired
    public JwtTokenProvider(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public String createAccessToken(
            User user
    ) throws JwtException {

        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("roles", user.getAuthorities());

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + accessTokenValidityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(
                        key,
                        SignatureAlgorithm.HS256
                )
                .compact();
    }


    public String createRefreshToken(User owner) throws EntityExistsException {
        String token = UUID.randomUUID().toString();
        tokenService.create(
                owner,
                token,
                new Date(new Date().getTime() + refreshTokenValidityInMilliseconds),
                TokenType.REFRESH
        );
        return token;
    }


    public String refreshToken(String refreshToken) throws AuthenticationException {

        Optional<Token> token = tokenService.getOptionalTokenByValue(refreshToken);

        if (token.isEmpty() || token.get().getExpirationDate().before(new Date()))
            throw new BadCredentialsException(
                    "Refresh token is invalid or expired! Please, login again to get new refresh and access tokens!");

        User user = token.get().getOwner();

        return createAccessToken(user);
    }


    public Authentication getAuthentication(String token) throws UsernameNotFoundException {
        UserDetails user = userService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(
                user, "", user.getAuthorities()
        );
    }


    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
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
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Jwt token is expired or invalid!");
        }
    }


    private List<String> getRoleNames(List<Role> roles) {
        return roles.stream().map(Role::getName).collect(Collectors.toList());
    }

}