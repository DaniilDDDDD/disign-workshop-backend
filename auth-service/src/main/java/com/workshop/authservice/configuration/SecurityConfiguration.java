package com.workshop.authservice.configuration;

import com.workshop.authservice.handler.OAuth2SuccessHandler;
import com.workshop.authservice.security.JwtFilterChainConfigurer;
import com.workshop.authservice.security.JwtTokenProvider;
import com.workshop.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfiguration {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final OAuth2SuccessHandler successHandler;


    @Autowired
    public SecurityConfiguration(
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            OAuth2SuccessHandler successHandler
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.successHandler = successHandler;
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
                        .antMatchers("/login**", "/register**", "/refresh**").permitAll()
                        .antMatchers("/oauth2**").permitAll()
                        .antMatchers("/").permitAll() //login page to test API
                        .antMatchers("/docs/**").hasAnyRole("ROLE_DEVELOPER", "ROLE_ADMIN")
                    .anyRequest().authenticated()
//                .and()
//                    .formLogin().permitAll()
                .and()
                    .apply(new JwtFilterChainConfigurer(jwtTokenProvider))
                .and()
                    .oauth2Login()
                            .authorizationEndpoint()
                            .baseUri("/oauth2/login")
                        .and()
                            .redirectionEndpoint()
                            .baseUri("/oauth2/callback/*")
                        .and()
                            .userInfoEndpoint()
                            .userService(userService)
                        .and()
                            .successHandler(successHandler)
//                            .failureHandler()
                .and().build();

    }

}
