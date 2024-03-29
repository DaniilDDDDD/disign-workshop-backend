package com.workshop.contentservice.configuration;

import com.workshop.contentservice.security.JwtFilterChainConfigurer;
import com.workshop.contentservice.security.JwtTokenProvider;
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


    @Autowired
    public SecurityConfiguration(
            JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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
                // TODO : баг с там, что мапа креденшелов не каститься в GrantedAuthorities
//                        .antMatchers(
//                                HttpMethod.GET,
//                                "/sketches/me**"
//                        ).hasRole("AUTHOR")
                        .antMatchers(
                                HttpMethod.GET,
                                "/sketches**",
                                "/sketches/**"
                        ).permitAll()
                        .antMatchers(
                                HttpMethod.GET,
                                "/tags**",
                                "/tags/**"
                        ).permitAll()
                // TODO : баг с там, что мапа креденшелов не каститься в GrantedAuthorities
//                        .antMatchers("/docs/**").hasAnyRole("DEVELOPER", "ADMIN")
                        .antMatchers("/docs/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .apply(new JwtFilterChainConfigurer(jwtTokenProvider))
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
