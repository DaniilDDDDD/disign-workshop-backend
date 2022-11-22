package com.workshop.contentservice.configuration;

import com.workshop.contentservice.security.JwtFilterChainConfigurer;
import com.workshop.contentservice.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public SecurityConfiguration(
            JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/sketch*").permitAll()
//                        .antMatchers("/tags*").hasAnyRole("ROLE_ADMIN", "ROLE_DEVELOPER", "ROLE_MODERATOR")
                        .antMatchers("/tags*").permitAll()
                        .antMatchers("/docs/**").hasAnyRole("ROLE_DEVELOPER", "ROLE_ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .apply(new JwtFilterChainConfigurer(jwtTokenProvider))
                .and().build();

    }

}
