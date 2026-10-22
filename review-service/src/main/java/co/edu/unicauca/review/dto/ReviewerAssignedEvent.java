package co.edu.unicauca.review.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/** Evento asíncrono publicado hacia Notification Service tras una asignación exitosa. */
public class ReviewerAssignedEvent implements Serializable {

    private Long questionId;
    private Long reviewerId;
    private LocalDateTime assignedAt;

    public ReviewerAssignedEvent() {}

    public ReviewerAssignedEvent(Long questionId, Long reviewerId) {
        this.questionId = questionId;
        this.reviewerId = reviewerId;
        this.assignedAt = LocalDateTime.now();
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
