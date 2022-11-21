package com.workshop.authservice.controller;

import com.workshop.authservice.configuration.DtoConfiguration;
import com.workshop.authservice.dto.user.UserInfo;
import com.workshop.authservice.dto.user.UserLogin;
import com.workshop.authservice.dto.user.UserRegister;
import com.workshop.authservice.dto.user.UserUpdate;
import com.workshop.authservice.model.User;
import com.workshop.authservice.security.JwtTokenProvider;
import com.workshop.authservice.service.UserService;
import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("")
@Tag(name = "User", description = "Users' endpoints")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Login user using email and password."
    )
    public ResponseEntity<UserLogin> login(
            @Validated({DtoConfiguration.OnRequest.class})
            @RequestBody
                    UserLogin userLogin
    ) throws AuthenticationException, JwtException, EntityExistsException {
        User user = userService.login(userLogin);
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);
        return ResponseEntity.ok().body(
                UserLogin.builder()
                        .login(user.getEmail())
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build()
        );
    }


    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh token",
            description = "Refresh user's access token using refresh token."
    )
    public ResponseEntity<UserLogin> refresh(
            @Validated({DtoConfiguration.OnRefreshToken.class})
            @RequestBody
                    UserLogin userLogin
    ) throws BadCredentialsException {
        return new ResponseEntity<>(
                UserLogin.builder()
                        .accessToken(
                                jwtTokenProvider.refreshToken(
                                        userLogin.getRefreshToken()
                                )
                        )
                        .refreshToken(userLogin.getRefreshToken())
                        .build(),
                HttpStatus.OK
        );
    }


    @PostMapping("/register")
    @Operation(
            summary = "Register user",
            description = "Register user using email as login field."
    )
    public ResponseEntity<UserInfo> register(
            @Valid
            @RequestBody
                    UserRegister userRegister
    ) throws EntityExistsException {
        User user = userService.create(userRegister);
        return new ResponseEntity<>(
                UserInfo.parseUser(user),
                HttpStatus.CREATED
        );
    }


    @GetMapping("/me")
    @Operation(
            summary = "Retrieve self",
            description = "Retrieve info about authenticated user."
    )
    public ResponseEntity<UserInfo> me(
            Authentication authentication
    ) throws EntityNotFoundException {
        return ResponseEntity.ok().body(
                UserInfo.parseUser(
                        userService.getUserByPrincipal(authentication.getPrincipal())
                )
        );
    }


    @GetMapping("")
    @Operation(
            summary = "Retrieve user",
            description = "Retrieve info about user by id."
    )
    public ResponseEntity<List<UserInfo>> listUsers(
            @RequestParam(value = "id", required = false)
            @Min(value = 1, message = "minimal value for id is 1")
                    Long id,
            @RequestParam(value = "email", required = false)
            @Email
                    String email,
            @RequestParam(value = "username", required = false)
                    String username,
            @RequestParam(value = "roles", required = false)
                    List<String> roles
    ) throws EntityExistsException {

        List<User> users = new LinkedList<>();

        if (id != null)
            users.add(userService.getUserById(id));
        else if (email != null)
            users.add(userService.getUserByEmail(email));
        else if (username != null)
            users.add(userService.getUserByUsername(username));
        else if (roles != null)
            users = userService.getUserWithRolesIn(roles);
        else
            users = userService.getUserWithRolesIn(List.of());

        return ResponseEntity.ok().body(
                users.stream().map(UserInfo::parseUserPublic).toList()
        );
    }


    // TODO: move avatar update to another endpoint
    @PostMapping(value = "/update", consumes = {"multipart/form-data"})
    @Operation(
            summary = "Update self",
            description = "Update authenticated user."
    )
    public ResponseEntity<UserInfo> update(
            @Valid
            @ModelAttribute
                    UserUpdate userUpdate,
            Authentication authentication
    ) throws IOException, EntityNotFoundException {
        Long id = (userService.getUserByPrincipal(authentication.getPrincipal())).getId();

        User user = userService.update(id, userUpdate);

        return ResponseEntity.ok().body(
                UserInfo.parseUser(user)
        );
    }


    @DeleteMapping("")
    @Operation(
            summary = "Delete self",
            description = "Delete authenticated user."
    )
    public ResponseEntity<String> delete(
            Authentication authentication
    ) throws IOException {
        User user = userService.getUserByPrincipal(authentication.getPrincipal());
        userService.delete(user);
        return ResponseEntity.noContent().build();
    }

}
