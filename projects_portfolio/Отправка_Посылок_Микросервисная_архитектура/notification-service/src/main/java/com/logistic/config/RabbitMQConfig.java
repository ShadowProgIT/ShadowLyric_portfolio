package com.logistic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistic.events.RoutingKeys;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATIONS_QUEUE = "q.notifications.all";

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jsonMessageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }

    @Bean
    public TopicExchange eventsExchange() {
        return ExchangeBuilder.topicExchange(RoutingKeys.EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable(NOTIFICATIONS_QUEUE).build();
    }

    @Bean
    public Binding notificationsBinding(Queue notificationsQueue, TopicExchange eventsExchange) {
        return BindingBuilder.bind(notificationsQueue).to(eventsExchange).with(RoutingKeys.ALL_EVENTS);
    }
}