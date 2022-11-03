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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
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
    ) throws AuthenticationException, JwtException {

        User user = userService.login(userLogin);

        String token = jwtTokenProvider.createToken(
                user.getEmail(),
                user.getAuthorities()
        );

        return ResponseEntity.ok().body(
                UserLogin.builder()
                        .login(user.getEmail())
                        .token(token)
                        .build()
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
    ) throws EntityExistsException {
        User user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok().body(
                UserInfo.parseUser(user)
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Retrieve user",
            description = "Retrieve info about user by id."
    )
    public ResponseEntity<UserInfo> retrieveUserPublicInfoById(
            @PathVariable("id")
                    Long id
    ) throws EntityExistsException {
        User user = userService.getUserById(id);
        return ResponseEntity.ok().body(
                UserInfo.parseUserPublic(user)
        );
    }

    @GetMapping("/{email}")
    @Operation(
            summary = "Retrieve user",
            description = "Retrieve info about user by id."
    )
    public ResponseEntity<UserInfo> retrieveUserPublicInfoByEmail(
            @PathVariable("email")
                    String email
    ) throws EntityExistsException {
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok().body(
                UserInfo.parseUserPublic(user)
        );
    }

    @GetMapping("")
    @Operation(
            summary = "Retrieve user",
            description = "Retrieve info about user by id."
    )
    public ResponseEntity<List<UserInfo>> listUsers(
            @RequestParam("roles")
                    List<String> roles
    ) throws EntityExistsException {

        List<String> publicRoles = List.of("ROLE_CUSTOMER", "ROLE_AUTHOR", "ROLE_EXECUTOR");
        List<User> users;

        if (!roles.isEmpty())
            users = userService.getUserWithRolesIn(publicRoles);

        roles.stream().filter(r -> !publicRoles.contains(r)).toList();
        users = userService.getUserWithRolesIn(roles);

        return ResponseEntity.ok().body(
                users.stream().map(UserInfo::parseUserPublic).toList()
        );
    }

    @PutMapping("")
    @Operation(
            summary = "Update self",
            description = "Update authenticated user."
    )
    public ResponseEntity<UserInfo> update(
            @Valid
            @RequestBody
                    UserUpdate userUpdate,
            Authentication authentication,
            MultipartFile multipartFile
    ) throws IOException {

        Long id = ((User) authentication.getPrincipal()).getId();

        User user = userService.update(id, userUpdate, multipartFile);

        return ResponseEntity.ok().body(
                UserInfo.parseUser(user)
        );
    }
}
