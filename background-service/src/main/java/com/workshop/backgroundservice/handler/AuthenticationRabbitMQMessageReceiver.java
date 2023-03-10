package com.workshop.backgroundservice.handler;


import com.workshop.backgroundservice.dto.user.UserOnConfirm;
import com.workshop.backgroundservice.model.user.InitializationToken;
import com.workshop.backgroundservice.service.AuthService;
import com.workshop.backgroundservice.service.email.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Component
@Profile("prod")
public class AuthenticationRabbitMQMessageReceiver {

    private String url;

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String route;

    private final AuthService authService;
    private final EmailService emailService;

    @Autowired
    public AuthenticationRabbitMQMessageReceiver(
            AuthService authService,
            EmailService emailService
    ) {
        this.authService = authService;
        this.emailService = emailService;
    }

    @PostConstruct
    private void initialize() {
        try (final DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.connect(InetAddress.getByName("8.8.8.8"), 12345);
            url = "http://" + datagramSocket.getLocalAddress().getHostAddress() + ":" + port + route + "/auth/";
        } catch (Exception ignored) {
            url = "http://localhost:" + port + route + "/auth/";
        }
    }


    @RabbitListener(queues = {"${rabbitmq.authentication-confirmation.queue-name}"})
    public void addUserForConfirmation(UserOnConfirm message) {
        try {
            InitializationToken token = authService.create(message.getEmail());
            emailService.sendEmail(
                    message.getEmail(),
                    "Authentication confirmation",
                    "Follow this link to confirm your account:\n" + url + token.getValue()
            );
        } catch (PersistenceException ignored) {
        }
    }

}
