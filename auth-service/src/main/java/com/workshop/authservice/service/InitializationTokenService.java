package com.workshop.authservice.service;

import com.workshop.authservice.model.User;
import com.workshop.authservice.repository.InitializationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class InitializationTokenService {

    private final InitializationTokenRepository initializationTokenRepository;

    @Autowired
    public InitializationTokenService(InitializationTokenRepository initializationTokenRepository) {
        this.initializationTokenRepository = initializationTokenRepository;
    }

    @Transactional
    public void deleteAllByUser(User user) {
        initializationTokenRepository.deleteAllByUserId(user.getId());
    }

}
