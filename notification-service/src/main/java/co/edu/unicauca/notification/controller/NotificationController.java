package co.edu.unicauca.notification.controller;

import co.edu.unicauca.notification.model.Notification;
import co.edu.unicauca.notification.repository.NotificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/** Expone las notificaciones procesadas, útil como evidencia en Postman del flujo asíncrono completo. */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository repository;

    public NotificationController(NotificationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Collection<Notification> listAll() {
        return repository.findAll();
    }

    @GetMapping("/recipient/{recipientId}")
    public Collection<Notification> listByRecipient(@PathVariable Long recipientId) {
        return repository.findByRecipient(recipientId);
    }
}
