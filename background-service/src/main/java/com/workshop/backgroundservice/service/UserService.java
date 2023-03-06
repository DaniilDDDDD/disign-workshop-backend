package com.workshop.backgroundservice.service;

import com.workshop.backgroundservice.model.user.InitializationToken;
import com.workshop.backgroundservice.model.user.Status;
import com.workshop.backgroundservice.model.user.User;
import com.workshop.backgroundservice.repository.authentication.InitializationTokenRepository;
import com.workshop.backgroundservice.repository.authentication.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

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

        if (user.getStatus() == Status.DISABLED) {
            initializationTokenRepository.delete(token);
            return false;
        } else
            user.setStatus(Status.ACTIVE);

        userRepository.save(user);
        initializationTokenRepository.delete(token);

        return true;
    }

}
