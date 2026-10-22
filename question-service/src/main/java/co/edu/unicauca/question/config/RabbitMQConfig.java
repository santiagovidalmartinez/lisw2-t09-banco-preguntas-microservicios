package co.edu.unicauca.question.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Topología de mensajería compartida del Banco de Preguntas Saber PRO.
 * Un único exchange directo ("saberpro.direct.exchange") enruta comandos
 * y eventos según su routing key. Question Service es quien PUBLICA el
 * comando SubmitQuestionForReview; declara aquí el exchange y la cola que
 * consume Lifecycle Service para que la topología quede garantizada incluso
 * si los servicios arrancan en distinto orden (declaración idempotente).
 */
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "saberpro.direct.exchange";
    public static final String QUEUE_SUBMIT_REVIEW = "question.submit.review.queue";
    public static final String ROUTING_KEY_SUBMIT = "question.submit";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue submitQueue() {
        return QueueBuilder.durable(QUEUE_SUBMIT_REVIEW).build();
    }

    @Bean
    public Binding bindingSubmit(Queue submitQueue, DirectExchange exchange) {
        return BindingBuilder.bind(submitQueue).to(exchange).with(ROUTING_KEY_SUBMIT);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
