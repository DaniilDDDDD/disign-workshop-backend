package com.workshop.authservice.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdate {

    private String username;
    private String firstName;
    private String lastName;
    private String bio;

}
