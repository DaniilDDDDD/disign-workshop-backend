package com.workshop.authservice.repository;

import com.workshop.authservice.model.Role;
import com.workshop.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);

    List<User> getAllByRolesIn(List<Role> roles);

    boolean existsUserByEmailOrUsername(String email, String username);

    void deleteUserByUsername(String username);
    void deleteUserByEmail(String email);

}
