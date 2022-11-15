package com.workshop.authservice.handler;

import com.google.gson.Gson;
import com.workshop.authservice.dto.user.UserLogin;
import com.workshop.authservice.model.User;
import com.workshop.authservice.security.JwtTokenProvider;
import com.workshop.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OAuth2SuccessHandler(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, EntityExistsException {

        String[] url = request.getRequestURI().split("/");

        User user = userService.oauth2Login(
                (OAuth2User) authentication.getPrincipal(),
                url[url.length - 1]
        );

        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(
                UserLogin.builder()
                        .login(user.getEmail())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build())
        );
        response.getWriter().flush();
    }
}
