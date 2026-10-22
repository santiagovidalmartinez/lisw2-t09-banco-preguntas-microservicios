package co.edu.unicauca.lifecycle.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/** Espejo del comando publicado por Question Service. */
public class SubmitCommandDTO implements Serializable {

    private Long questionId;
    private String operationId;
    private String requestedBy;
    private LocalDateTime requestedAt;

    public SubmitCommandDTO() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getOperationId() { return operationId; }
    public void setOperationId(String operationId) { this.operationId = operationId; }
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}
