package com.workshop.authservice.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class User implements Serializable, UserDetails, OAuth2User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Convert(converter = Status.StatusConverter.class)
    private Status status;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "must contain email address")
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "biography")
    private String bio;

    @Column(name = "avatar")
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private List<Role> roles;

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return status == Status.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != Status.DISABLED && status != Status.INITIALIZED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // change when password would be able to be expired
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status != Status.INITIALIZED;
    }

    @Override
    public String getName() {
        return this.email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("username", this.username);
        attributes.put("email", this.email);
        attributes.put("firstName", this.firstName);
        attributes.put("lastName", this.lastName);
        attributes.put("roles", this.roles);
        attributes.put("status", this.status);
        attributes.put("bio", this.bio);
        attributes.put("avatar", this.avatar);
        return attributes;
    }
}
