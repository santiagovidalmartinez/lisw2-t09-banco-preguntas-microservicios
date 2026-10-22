package co.edu.unicauca.question.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Comando asíncrono publicado en RabbitMQ: solicita al Lifecycle Service
 * transicionar una pregunta de DRAFT a PENDING_REVIEW. Debe ser Serializable
 * (o convertirse a JSON vía Jackson2JsonMessageConverter, como aquí).
 */
public class SubmitQuestionCommand implements Serializable {

    private Long questionId;
    private String operationId;
    private String requestedBy;
    private LocalDateTime requestedAt;

    public SubmitQuestionCommand() {}

    public SubmitQuestionCommand(Long questionId, String operationId, String requestedBy) {
        this.questionId = questionId;
        this.operationId = operationId;
        this.requestedBy = requestedBy;
        this.requestedAt = LocalDateTime.now();
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public String getOperationId() { return operationId; }
    public void setOperationId(String operationId) { this.operationId = operationId; }
    public String getRequestedBy() { return requestedBy; }
    public void setRequestedBy(String requestedBy) { this.requestedBy = requestedBy; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}
