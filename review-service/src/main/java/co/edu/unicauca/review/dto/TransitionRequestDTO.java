package co.edu.unicauca.review.dto;

public class TransitionRequestDTO {
    private String targetState;
    private String changedBy;
    private String comment;

    public TransitionRequestDTO() {}

    public TransitionRequestDTO(String targetState, String changedBy, String comment) {
        this.targetState = targetState;
        this.changedBy = changedBy;
        this.comment = comment;
    }

    public String getTargetState() { return targetState; }
    public void setTargetState(String targetState) { this.targetState = targetState; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
