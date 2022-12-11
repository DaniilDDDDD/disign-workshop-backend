package com.workshop.metadataservice.security;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JwtAuthentication implements Authentication {

    private final List<GrantedAuthority> authorities;

    private final Map<String, Object> credentials;

    private final String principal;

    private boolean authenticated;

    public JwtAuthentication(
            String principal,
            Map<String, Object> credentials,
            Collection<? extends GrantedAuthority> authorities
    ) {
        if (authorities == null) {
            this.authorities = AuthorityUtils.NO_AUTHORITIES;
        } else {
            this.authorities = List.copyOf(authorities);
        }
        authenticated = true;
        this.principal = principal;
        this.credentials = credentials;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal;
    }
}
