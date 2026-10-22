package co.edu.unicauca.review.model;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Review {

    public enum ReviewStatus { ASSIGNED, APPROVED, REJECTED }

    private static final AtomicLong SEQUENCE = new AtomicLong(1);

    private final Long id;
    private final Long questionId;
    private final Long reviewerId;
    private final LocalDateTime assignedAt;
    private ReviewStatus status;
    private String comments;
    private LocalDateTime resolvedAt;

    public Review(Long questionId, Long reviewerId) {
        this.id = SEQUENCE.incrementAndGet();
        this.questionId = questionId;
        this.reviewerId = reviewerId;
        this.assignedAt = LocalDateTime.now();
        this.status = ReviewStatus.ASSIGNED;
    }

    public void resolve(ReviewStatus status, String comments) {
        this.status = status;
        this.comments = comments;
        this.resolvedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getQuestionId() { return questionId; }
    public Long getReviewerId() { return reviewerId; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public ReviewStatus getStatus() { return status; }
    public String getComments() { return comments; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
}
