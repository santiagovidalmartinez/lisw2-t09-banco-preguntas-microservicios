package co.edu.unicauca.notification.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReviewerAssignedEvent implements Serializable {
    private Long questionId;
    private Long reviewerId;
    private LocalDateTime assignedAt;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
}
