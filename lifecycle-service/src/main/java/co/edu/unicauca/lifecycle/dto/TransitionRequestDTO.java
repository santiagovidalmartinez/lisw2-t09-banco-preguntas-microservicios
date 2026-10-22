package co.edu.unicauca.lifecycle.dto;

public class TransitionRequestDTO {

    private String targetState;
    private String changedBy;
    private String comment;

    public String getTargetState() { return targetState; }
    public void setTargetState(String targetState) { this.targetState = targetState; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
