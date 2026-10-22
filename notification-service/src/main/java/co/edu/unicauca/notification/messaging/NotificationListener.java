package co.edu.unicauca.notification.messaging;

import co.edu.unicauca.notification.dto.ReviewerAssignedEvent;
import co.edu.unicauca.notification.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Comunicación asíncrona (AMQP): consume el evento ReviewerAssigned publicado por Review Service. */
@Component
public class NotificationListener {

    private final EmailService emailService;

    public NotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "reviewer.assigned.queue")
    public void handleReviewerAssigned(ReviewerAssignedEvent event) {
        System.out.println("[NotificationService] Evento ReviewerAssigned recibido. questionId="
                + event.getQuestionId() + " reviewerId=" + event.getReviewerId());
        emailService.sendReviewerAssignedNotification(event.getReviewerId(), event.getQuestionId());
    }
}
