package com.workshop.backgroundservice.service;

import com.workshop.backgroundservice.dto.user.UserOnConfirm;
import com.workshop.backgroundservice.model.user.InitializationToken;
import com.workshop.backgroundservice.model.user.Status;
import com.workshop.backgroundservice.model.user.User;
import com.workshop.backgroundservice.repository.authentication.InitializationTokenRepository;
import com.workshop.backgroundservice.repository.authentication.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Value("${initialization.expired}")
    private long initializationValidityInMilliseconds;

    private final UserRepository userRepository;
    private final InitializationTokenRepository initializationTokenRepository;

    @Autowired
    public UserService(UserRepository userRepository, InitializationTokenRepository initializationTokenRepository) {
        this.userRepository = userRepository;
        this.initializationTokenRepository = initializationTokenRepository;
    }


    public boolean confirm(String tokenValue) {

        Optional<InitializationToken> initializationToken = initializationTokenRepository.findByValue(tokenValue);

        if (initializationToken.isEmpty())
            return false;

        InitializationToken token = initializationToken.get();
        User user = token.getUser();

        user.setStatus(Status.ACTIVE);

        userRepository.save(user);
        initializationTokenRepository.delete(token);

        return true;
    }


    public InitializationToken create(UserOnConfirm userOnConfirm) throws EntityNotFoundException {

        Optional<User> user = userRepository.findUserByEmail(userOnConfirm.getEmail());
        if (user.isEmpty())
            throw new EntityNotFoundException("No user with provided email!");

        InitializationToken token = InitializationToken.builder()
                .value(UUID.randomUUID().toString())
                .expirationDate(new Date(new Date().getTime() + initializationValidityInMilliseconds))
                .user(user.get())
                .build();

        return initializationTokenRepository.save(token);
    }

}
