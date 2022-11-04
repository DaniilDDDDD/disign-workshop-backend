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
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.LinkedList;
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
        User user = userService.getUserByEmail(((User) authentication.getPrincipal()).getEmail());
        return ResponseEntity.ok().body(
                UserInfo.parseUser(user)
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


    @DeleteMapping("")
    @Operation(
            summary = "Delete self",
            description = "Delete authenticated user."
    )
    public ResponseEntity<String> delete(
            Authentication authentication
    ) throws IOException {
        User user = (User) authentication.getPrincipal();
        userService.delete(user);
        return new ResponseEntity<>(
                "User " + user.getEmail() + " deleted!",
                HttpStatus.NO_CONTENT
        );
    }

}
