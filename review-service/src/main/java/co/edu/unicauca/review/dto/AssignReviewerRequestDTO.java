package co.edu.unicauca.review.dto;

public class AssignReviewerRequestDTO {
    private Long questionId;
    private Long reviewerId;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
}
