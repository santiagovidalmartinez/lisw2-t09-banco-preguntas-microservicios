package co.edu.unicauca.notification.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Notification {

    public enum NotificationType { REVIEWER_ASSIGNED, QUESTION_APPROVED, QUESTION_REJECTED }
    public enum NotificationStatus { PENDING, SENT }

    private static final AtomicLong SEQUENCE = new AtomicLong(1);

    private final Long id;
    private final Long recipientId;
    private final NotificationType type;
    private final String subject;
    private final String message;
    private NotificationStatus status;
    private LocalDateTime sentAt;

    public Notification(Long recipientId, NotificationType type, String subject, String message) {
        this.id = SEQUENCE.incrementAndGet();
        this.recipientId = recipientId;
        this.type = type;
        this.subject = subject;
        this.message = message;
        this.status = NotificationStatus.PENDING;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getRecipientId() { return recipientId; }
    public NotificationType getType() { return type; }
    public String getSubject() { return subject; }
    public String getMessage() { return message; }
    public NotificationStatus getStatus() { return status; }
    public LocalDateTime getSentAt() { return sentAt; }
}
