package co.edu.unicauca.review.dto;

/** Espejo parcial de QuestionState del Lifecycle Service (solo lo que Review Service necesita). */
public class QuestionStateDTO {
    private Long questionId;
    private String state;

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
