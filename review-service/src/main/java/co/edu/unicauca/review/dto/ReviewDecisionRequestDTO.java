package co.edu.unicauca.review.dto;

public class ReviewDecisionRequestDTO {
    /** APPROVED o REJECTED */
    private String decision;
    private String comments;

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
