package co.edu.unicauca.lifecycle.messaging;

import co.edu.unicauca.lifecycle.dto.SubmitCommandDTO;
import co.edu.unicauca.lifecycle.model.QuestionStatus;
import co.edu.unicauca.lifecycle.service.LifecycleService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Comunicación asíncrona (AMQP): consume el comando SubmitQuestionForReview
 * publicado por Question Service y procesa la transición DRAFT -> PENDING_REVIEW.
 */
@Component
public class CommandListener {

    private final LifecycleService lifecycleService;

    public CommandListener(LifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    @RabbitListener(queues = "question.submit.review.queue")
    public void handleSubmitForReview(SubmitCommandDTO command) {
        System.out.println("[LifecycleService] Comando SubmitQuestionForReview recibido. questionId="
                + command.getQuestionId() + " operationId=" + command.getOperationId());

        lifecycleService.processTransition(command.getQuestionId(), QuestionStatus.PENDING_REVIEW, command.getOperationId());
    }
}
