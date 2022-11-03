package com.workshop.authservice.service;

import com.workshop.authservice.dto.user.UserLogin;
import com.workshop.authservice.dto.user.UserRegister;
import com.workshop.authservice.dto.user.UserUpdate;
import com.workshop.authservice.model.Role;
import com.workshop.authservice.model.Status;
import com.workshop.authservice.model.User;
import com.workshop.authservice.repository.RoleRepository;
import com.workshop.authservice.repository.UserRepository;
import com.workshop.authservice.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Value("filesRoot")
    private String filesRoot;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) throws EntityNotFoundException {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isEmpty()) throw new EntityNotFoundException("No user with email " + email);
        return user.get();
    }

    public User getUserById(Long id) throws EntityNotFoundException {
        Optional<User> user = userRepository.getUserById(id);
        if (user.isEmpty()) throw new EntityNotFoundException("No user with id " + id);
        return user.get();
    }

    public List<User> getUserWithRolesIn(List<String> roles) {
        List<Role> rolesData = roles.stream().map(roleName -> {
            Optional<Role> role = roleRepository.getRoleByName(roleName);
            return role.isEmpty() ? null : role.get();
        }).filter(Objects::nonNull).toList();
        return userRepository.getAllByRolesIn(rolesData);
    }

    public User login(UserLogin login) throws AuthenticationException {

        Optional<User> user = userRepository.getUserByEmail(login.getLogin());

        if (
                user.isPresent() &&
                        passwordEncoder.matches(
                                login.getPassword(), user.get().getPassword()
                        )
        )
            return user.get();

        throw new BadCredentialsException("Login or password are wrong!");
    }


    public User create(UserRegister userRegister) throws EntityExistsException {

        if (userRepository.existsUserByEmailOrUsername(userRegister.getEmail(), userRegister.getUsername()))
            throw new EntityExistsException("User with provided email or username already exists!");

        User user = User.builder()
                .email(userRegister.getEmail())
                .username(userRegister.getUsername())
                .firstName(userRegister.getFirstName())
                .lastName(userRegister.getLastName())
                // TODO: change status to INITIALIZED when authentication confirmation would be created
                .status(Status.ACTIVE)
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .build();

        Role role = userRegister.getRole() != null ?
                roleRepository.getRoleByName(userRegister.getRole()).get() :
                roleRepository.getRoleByName("ROLE_CUSTOMER").get();
        user.setRoles(List.of(role));

        return userRepository.save(user);

    }

    public User update(
            Long id,
            UserUpdate userUpdate,
            MultipartFile multipartFile
    ) throws IOException {
        Optional<User> userData = userRepository.getUserById(id);
        if (userData.isEmpty()) throw new EntityNotFoundException("User with id " + id + "not found!");

        User user = userData.get();

        String fileName = user.getAvatar();
        if (multipartFile != null) {
            fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            if (user.getAvatar() == null) {
                String uploadDir = this.filesRoot + id + "/";
                FileUtil.saveFile(uploadDir, fileName, multipartFile);
            }
            else {
                FileUtil.updateFile(user.getAvatar(), fileName, multipartFile);
            }
        }

        user.setUsername(userUpdate.getUsername() != null ? userUpdate.getUsername() : user.getUsername());
        user.setFirstName(userUpdate.getFirstName() != null ? userUpdate.getFirstName() : user.getFirstName());
        user.setLastName(userUpdate.getLastName() != null ? userUpdate.getLastName() : user.getLastName());
        user.setBio(userUpdate.getBio() != null ? userUpdate.getBio() : user.getBio());
        user.setAvatar(fileName);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("User with email " + email + " not found!");
        return user.get();
    }

}
