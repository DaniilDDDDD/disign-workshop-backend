package com.workshop.authservice.service;

import com.workshop.authservice.dto.user.UserLogin;
import com.workshop.authservice.dto.user.UserRegister;
import com.workshop.authservice.dto.user.UserUpdate;
import com.workshop.authservice.model.LoginSource;
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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService extends DefaultOAuth2UserService implements UserDetailsService {

    @Value("${filesRoot}")
    private String filesRoot;

    @Value("${publicRoles}")
    private List<String> publicRoleNames;

    @Value("${adminRoles}")
    private List<String> adminRoleNames;

    private List<Role> publicRoles;

    private List<Role> adminRoles;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @PostConstruct
    private void initialization() {
        publicRoles = publicRoleNames.stream()
                .map(roleName -> {
                    Optional<Role> role = roleRepository.getRoleByName(roleName);
                    if (role.isEmpty())
                        return roleRepository.save(Role.builder().name(roleName).build());
                    return role.get();
                })
                .toList();

        adminRoles = adminRoleNames.stream()
                .map(roleName -> {
                    Optional<Role> role = roleRepository.getRoleByName(roleName);
                    if (role.isEmpty())
                        return roleRepository.save(Role.builder().name(roleName).build());
                    return role.get();
                })
                .toList();
    }


    public User getUserByPrincipal(Object principal) throws EntityNotFoundException {
        if (principal.getClass().equals(User.class))
            return (User) principal;
        return getUserByEmail(((DefaultOAuth2User) principal).getAttribute("email"));
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


    public User getUserByUsername(String username) throws EntityNotFoundException {
        Optional<User> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()) throw new EntityNotFoundException("No user with username " + username);
        return user.get();
    }


    public List<User> getUserWithRolesIn(List<String> roles) {
        roles = roles.stream().filter(r -> publicRoleNames.contains(r)).toList();
        if (roles.isEmpty())
            return userRepository.getAllByRolesIn(publicRoles);
        return userRepository.getAllByRolesIn(
                roles.stream()
                        .map(roleName -> {
                            for (Role role : publicRoles)
                                if (Objects.equals(roleName, role.getName()))
                                    return role;
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .toList()
        );
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


    public User oauth2Login(OAuth2User userData, String provider) throws IllegalArgumentException {

        LoginSource source = LoginSource.getByName(provider);
        Map<String, Object> attributes = userData.getAttributes();

        if (source == LoginSource.GOOGLE) {
            Optional<User> principal = userRepository.getUserByEmail((String) attributes.get("email"));

            if (principal.isEmpty())
                return userRepository.save(
                        User.builder()
                                .email((String) attributes.get("email"))
                                .username((String) attributes.get("name"))
                                .firstName((String) attributes.get("given_name"))
                                .lastName((String) attributes.get("family_name"))
                                .avatar((String) attributes.get("picture"))
                                .roles(List.of(publicRoles.get(0)))
                                .status(Status.ACTIVE) // using oauth2 means that user is verified
                                .loginSource(source)
                                .build());

            // update user with provided data
            User user = principal.get();
            user.setUsername((String) attributes.get("name"));
            user.setFirstName((String) attributes.get("given_name"));
            user.setLastName((String) attributes.get("family_name"));
            return userRepository.save(user);

        } else
            throw new IllegalArgumentException("Provided OAuth2 source is not available!");

    }


    public User create(UserRegister userRegister) throws EntityExistsException {

        if (userRepository.existsUserByEmailOrUsername(userRegister.getEmail(), userRegister.getUsername()))
            throw new EntityExistsException("User with provided email or username already exists!");

        User user = User.builder()
                .email(userRegister.getEmail())
                .username(userRegister.getUsername())
                .firstName(userRegister.getFirstName())
                .lastName(userRegister.getLastName())
                .bio(userRegister.getBio())
                .status(Status.DISABLED)
                .loginSource(LoginSource.LOCAL)
                .password(passwordEncoder.encode(userRegister.getPassword()))
                .build();

        Role role = publicRoles.get(0);
        if (userRegister.getRole() != null) {
            for (Role r : publicRoles)
                if (Objects.equals(r.getName(), userRegister.getRole())) {
                    role = r;
                    break;
                }
        }
        user.setRoles(List.of(role));

        return userRepository.save(user);
    }


    public User update(
            Long id,
            UserUpdate userUpdate
    ) throws IOException {

        Optional<User> userData = userRepository.getUserById(id);
        if (userData.isEmpty()) throw new EntityNotFoundException("User with id " + id + "not found!");

        User user = userData.get();

        user.setUsername(userUpdate.getUsername() != null ? userUpdate.getUsername() : user.getUsername());
        user.setFirstName(userUpdate.getFirstName() != null ? userUpdate.getFirstName() : user.getFirstName());
        user.setLastName(userUpdate.getLastName() != null ? userUpdate.getLastName() : user.getLastName());
        user.setBio(userUpdate.getBio() != null ? userUpdate.getBio() : user.getBio());

        // If new avatar image is null set null into entity, else override image.
        String fileName = user.getAvatar();
        if (userUpdate.getAvatar() != null) {
            fileName = StringUtils.cleanPath(userUpdate.getAvatar().getOriginalFilename());
            String uploadDir = this.filesRoot + id + "/";
            if (user.getAvatar() == null) {
                FileUtil.saveFile(uploadDir, fileName, userUpdate.getAvatar());
                fileName = uploadDir + fileName;
            } else {
                FileUtil.updateFile(user.getAvatar() + fileName, fileName, userUpdate.getAvatar());
                fileName = user.getAvatar();
            }
        } else if (user.getAvatar() != null) {
            FileUtil.deleteFile(user.getAvatar());
            fileName = null;
        }
        user.setAvatar(fileName);

        // If new list of roles contains admin roles discard them.
        // If result list is empty set all public roles into entity.
        List<Role> roles = user.getRoles();
        if (userUpdate.getRoles() != null) {
            roles = userUpdate.getRoles().stream()
                    .map(r -> publicRoleNames.indexOf(r))
                    .filter(i -> i != -1)
                    .map(publicRoles::get)
                    .toList();
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }


    @Transactional
    public void delete(User user) throws IOException {
        if (user.getAvatar() != null)
            FileUtil.deleteFile(user.getAvatar());
        userRepository.delete(user);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.getUserByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("User with email " + email + " not found!");
        return user.get();
    }

}
