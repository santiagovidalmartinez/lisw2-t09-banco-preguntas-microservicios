package co.edu.unicauca.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "saberpro.direct.exchange";
    public static final String QUEUE_REVIEWER_ASSIGNED = "reviewer.assigned.queue";
    public static final String ROUTING_KEY_REVIEWER_ASSIGNED = "reviewer.assigned";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue reviewerAssignedQueue() {
        return QueueBuilder.durable(QUEUE_REVIEWER_ASSIGNED).build();
    }

    @Bean
    public Binding bindingReviewerAssigned(Queue reviewerAssignedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(reviewerAssignedQueue).to(exchange).with(ROUTING_KEY_REVIEWER_ASSIGNED);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
