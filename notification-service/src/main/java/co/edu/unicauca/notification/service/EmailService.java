package co.edu.unicauca.notification.service;

import co.edu.unicauca.notification.model.Notification;
import co.edu.unicauca.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

/**
 * Simula el envío de notificaciones (correo/push) sin bloquear el flujo
 * principal de asignación de revisor: se ejecuta en el hilo del listener
 * de RabbitMQ, totalmente desacoplado de la petición HTTP original.
 */
@Service
public class EmailService {

    private final NotificationRepository repository;

    public EmailService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification sendReviewerAssignedNotification(Long reviewerId, Long questionId) {
        Notification notification = new Notification(
                reviewerId,
                Notification.NotificationType.REVIEWER_ASSIGNED,
                "Nueva pregunta asignada para revisión",
                "Se te ha asignado la pregunta #" + questionId + " para revisión por pares en el Banco de Preguntas Saber PRO.");

        // Simulación de envío diferido (latencia de red de un proveedor de correo real).
        try {
            Thread.sleep(150);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        notification.markAsSent();
        repository.save(notification);

        System.out.println("[NotificationService] Notificación enviada a revisorId=" + reviewerId
                + " sobre la pregunta #" + questionId);
        return notification;
    }
}
