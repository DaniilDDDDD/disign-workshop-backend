package com.workshop.authservice.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRegister {

    @NotNull(message = "username field is not provided")
    private String username;

    @NotNull(message = "email field is not provided")
    @NotBlank(message = "email field must not be blank")
    private String email;

    @NotNull(message = "password field is not provided")
    private String password;

    private String firstName;

    private String lastName;

    private String bio;

    private String role;

}
