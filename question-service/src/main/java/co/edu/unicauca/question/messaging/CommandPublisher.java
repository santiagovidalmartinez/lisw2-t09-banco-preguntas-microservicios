package co.edu.unicauca.question.messaging;

import co.edu.unicauca.question.config.RabbitMQConfig;
import co.edu.unicauca.question.dto.SubmitQuestionCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Comunicación asíncrona (AMQP): publica el comando SubmitQuestionForReview
 * dirigido al Lifecycle Service, sin esperar su procesamiento.
 */
@Component
public class CommandPublisher {

    private final RabbitTemplate rabbitTemplate;

    public CommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishSubmitForReview(SubmitQuestionCommand command) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_SUBMIT, command);
    }
}
