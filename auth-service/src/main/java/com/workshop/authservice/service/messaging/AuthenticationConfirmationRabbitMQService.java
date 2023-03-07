package com.workshop.authservice.service.messaging;


import com.workshop.authservice.dto.user.UserOnConfirm;
import com.workshop.authservice.model.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationConfirmationRabbitMQService {

    @Value("${rabbitmq.authentication-confirmation.routing-key}")
    private String routingKey;

    @Value("${rabbitmq.authentication-confirmation.topic-exchange-name}")
    private String exchange;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AuthenticationConfirmationRabbitMQService(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendConfirmation(User user) {
        rabbitTemplate.convertAndSend(
                exchange,
                routingKey,
                UserOnConfirm.builder().email(user.getEmail()).build()
        );
    }
}
