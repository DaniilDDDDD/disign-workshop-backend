package com.workshop.authservice.dto.user;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
public class UserUpdate {

    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private List<String> roles;
    private MultipartFile avatar;

}
