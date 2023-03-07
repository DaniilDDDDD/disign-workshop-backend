package com.workshop.backgroundservice.configuration.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfirmationRabbitMQConfiguration {

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Value("${rabbitmq.authentication-confirmation.queue-name}")
    private String queueName;

    @Value("${rabbitmq.authentication-confirmation.topic-exchange-name}")
    private String topicExchangeName;

    @Value("${rabbitmq.authentication-confirmation.routing-key}")
    private String routingKey;


    @Bean(name = "confirmationQueue")
    Queue queue() {
        return new Queue(queueName, true);
    }


    @Bean(name = "authenticationExchange")
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }


    @Bean(name = "authenticationConfirmationBinding")
    Binding binding(
            @Qualifier("confirmationQueue") Queue queue,
            @Qualifier("authenticationExchange") TopicExchange topicExchange
    ) {
        return BindingBuilder.bind(queue).to(topicExchange).with(routingKey);
    }


    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

}
