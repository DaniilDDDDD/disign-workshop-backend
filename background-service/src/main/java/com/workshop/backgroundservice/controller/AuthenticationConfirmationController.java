package com.workshop.backgroundservice.controller;

import com.workshop.backgroundservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthenticationConfirmation", description = "AuthenticationConfirmation's endpoints")
public class AuthenticationConfirmationController {

    private final UserService userService;

    @Autowired
    public AuthenticationConfirmationController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{token}")
    @Operation(
            summary = "Confirm authentication",
            description = "Confirm user authentication via token, send to user's email"
    )
    public ResponseEntity<String> confirm(
            @PathVariable String token
    ) {
        String result = userService.confirm(token) ?
                "User was confirmed!" :
                "User was not confirmed!";

        return ResponseEntity.ok(result);
    }
}
