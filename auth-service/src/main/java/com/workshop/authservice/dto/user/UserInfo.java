package com.workshop.authservice.dto.user;


import com.workshop.authservice.model.Role;
import com.workshop.authservice.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfo {

    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String status;
    private String bio;
    private String avatar;
    private String loginSource;
    private List<String> roles;

    public static UserInfo parseUser(User user) {
        /*
         * Return UserInfo containing all information of provided user
         * */
        return UserInfo.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .avatar(user.getAvatar())
                .loginSource(user.getLoginSource().getName())
                .roles(user.getRoles().stream().map(Role::getName).toList())
                .status(user.getStatus().getName())
                .build();
    }

    public static UserInfo parseUserPublic(User user) {
        /*
        * Return UserInfo containing public information of provided user
        * */
        return UserInfo.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .avatar(user.getAvatar())
                .build();
    }

}
