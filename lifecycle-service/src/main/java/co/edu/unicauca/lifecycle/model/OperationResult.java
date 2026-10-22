package co.edu.unicauca.lifecycle.model;

import java.time.LocalDateTime;

/**
 * Registro del resultado de una operación asíncrona, consultable vía
 * GET /operations/{operationId}. Permite al cliente verificar si una
 * transición solicitada (por ejemplo SubmitQuestionForReview) terminó
 * en éxito o en fallo, sin que la respuesta 202 Accepted inicial
 * garantice nada sobre el resultado final (punto 5/6 del enunciado).
 */
public class OperationResult {

    public enum Status { PENDING, COMPLETED, FAILED }

    private final String operationId;
    private final Long questionId;
    private Status status;
    private String message;
    private final LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public OperationResult(String operationId, Long questionId) {
        this.operationId = operationId;
        this.questionId = questionId;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void complete(String message) {
        this.status = Status.COMPLETED;
        this.message = message;
        this.completedAt = LocalDateTime.now();
    }

    public void fail(String message) {
        this.status = Status.FAILED;
        this.message = message;
        this.completedAt = LocalDateTime.now();
    }

    public String getOperationId() { return operationId; }
    public Long getQuestionId() { return questionId; }
    public Status getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
}
