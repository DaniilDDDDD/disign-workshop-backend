package com.workshop.authservice.configuration;

import com.workshop.authservice.handler.OAuth2FailureHandler;
import com.workshop.authservice.handler.OAuth2SuccessHandler;
import com.workshop.authservice.security.JwtFilterChainConfigurer;
import com.workshop.authservice.security.JwtTokenProvider;
import com.workshop.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
    private final OAuth2FailureHandler failureHandler;


    @Autowired
    public SecurityConfiguration(
            JwtTokenProvider jwtTokenProvider,
            UserService userService,
            OAuth2SuccessHandler successHandler,
            OAuth2FailureHandler failureHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                    .cors(Customizer.withDefaults())
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                    .authorizeRequests()
                        .antMatchers(
                                "/login**",
                                "/register**",
                                "/refresh**",
                                "/resource**"
                        ).permitAll()
                        .antMatchers(HttpMethod.GET, "/").permitAll()
                        .antMatchers("/oauth2**").permitAll()
                // TODO : баг с там, что мапа креденшелов не каститься в GrantedAuthorities
//                        .antMatchers("/docs/**").hasAnyRole("DEVELOPER", "ADMIN")
                        .antMatchers("/docs/**").permitAll()
                    .anyRequest().authenticated()
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
                            .failureHandler(failureHandler)
                .and().build();

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
