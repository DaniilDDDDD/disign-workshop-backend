package com.workshop.backgroundservice.handler;


import com.workshop.backgroundservice.dto.user.UserOnConfirm;
import com.workshop.backgroundservice.model.user.InitializationToken;
import com.workshop.backgroundservice.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PersistenceException;

@Component
public class AuthenticationRabbitMQMessageReceiver {

    private final UserService userService;

    @Autowired
    public AuthenticationRabbitMQMessageReceiver(UserService userService) {
        this.userService = userService;
    }


    @RabbitListener(queues = {"${rabbitmq.authentication-confirmation.queue-name}"})
    public void addUserForConfirmation(UserOnConfirm message) {
        try {
            InitializationToken token = userService.create(message);
            // TODO: send email with token
        } catch (PersistenceException ignored){}
    }

}
