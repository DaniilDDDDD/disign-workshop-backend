package com.workshop.backgroundservice.controller;

import com.workshop.backgroundservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "AuthenticationConfirmation", description = "AuthenticationConfirmation's endpoints")
public class AuthenticationConfirmationController {

    private final AuthService authService;

    @Autowired
    public AuthenticationConfirmationController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping("/{token}")
    @Operation(
            summary = "Confirm authentication",
            description = "Confirm user authentication via token, send to user's email"
    )
    public ResponseEntity<String> confirm(
            @PathVariable String token
    ) {
        String result = authService.confirm(token) ?
                "User was confirmed!" :
                "User was not confirmed!";

        return ResponseEntity.ok(result);
    }
}
