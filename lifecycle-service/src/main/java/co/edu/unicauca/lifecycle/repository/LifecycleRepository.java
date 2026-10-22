package co.edu.unicauca.lifecycle.repository;

import co.edu.unicauca.lifecycle.model.OperationResult;
import co.edu.unicauca.lifecycle.model.QuestionState;
import co.edu.unicauca.lifecycle.model.QuestionStatus;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LifecycleRepository {

    private final Map<Long, QuestionState> estados = new ConcurrentHashMap<>();
    private final Map<String, OperationResult> operaciones = new ConcurrentHashMap<>();

    /** Si la pregunta aún no tiene estado registrado, se asume DRAFT (estado inicial por defecto). */
    public QuestionState getOrCreate(Long questionId) {
        return estados.computeIfAbsent(questionId, id -> new QuestionState(id, QuestionStatus.DRAFT));
    }

    public Optional<QuestionState> find(Long questionId) {
        return Optional.ofNullable(estados.get(questionId));
    }

    public void saveOperation(OperationResult result) {
        operaciones.put(result.getOperationId(), result);
    }

    public Optional<OperationResult> findOperation(String operationId) {
        return Optional.ofNullable(operaciones.get(operationId));
    }
}
