package co.edu.unicauca.lifecycle.model;

import java.time.LocalDateTime;

public class QuestionState {

    private final Long questionId;
    private QuestionStatus state;
    private LocalDateTime changedAt;
    private String changedBy;
    private String comment;

    public QuestionState(Long questionId, QuestionStatus initialState) {
        this.questionId = questionId;
        this.state = initialState;
        this.changedAt = LocalDateTime.now();
    }

    public void transitionTo(QuestionStatus newState, String changedBy, String comment) {
        this.state = newState;
        this.changedAt = LocalDateTime.now();
        this.changedBy = changedBy;
        this.comment = comment;
    }

    public Long getQuestionId() { return questionId; }
    public QuestionStatus getState() { return state; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public String getChangedBy() { return changedBy; }
    public String getComment() { return comment; }
}
