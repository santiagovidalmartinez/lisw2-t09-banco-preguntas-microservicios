package co.edu.unicauca.lifecycle.service;

import co.edu.unicauca.lifecycle.model.OperationResult;
import co.edu.unicauca.lifecycle.model.QuestionState;
import co.edu.unicauca.lifecycle.model.QuestionStatus;
import co.edu.unicauca.lifecycle.repository.LifecycleRepository;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Autoridad única sobre el estado y las transiciones de una pregunta.
 * Los demás servicios (Question, Review) SOLICITAN transiciones a través
 * de este servicio; ninguno actualiza el estado por su cuenta.
 */
@Service
public class LifecycleService {

    private final LifecycleRepository repository;

    /** Matriz de transiciones válidas del ciclo de vida oficial de la pregunta. */
    private static final Map<QuestionStatus, Set<QuestionStatus>> TRANSICIONES_VALIDAS = new EnumMap<>(QuestionStatus.class);
    static {
        TRANSICIONES_VALIDAS.put(QuestionStatus.DRAFT, EnumSet.of(QuestionStatus.PENDING_REVIEW));
        TRANSICIONES_VALIDAS.put(QuestionStatus.PENDING_REVIEW, EnumSet.of(QuestionStatus.IN_REVIEW));
        TRANSICIONES_VALIDAS.put(QuestionStatus.IN_REVIEW, EnumSet.of(QuestionStatus.APPROVED, QuestionStatus.REJECTED));
        TRANSICIONES_VALIDAS.put(QuestionStatus.REJECTED, EnumSet.of(QuestionStatus.PENDING_REVIEW)); // corrección y reenvío
        TRANSICIONES_VALIDAS.put(QuestionStatus.APPROVED, EnumSet.noneOf(QuestionStatus.class)); // estado terminal
    }

    public LifecycleService(LifecycleRepository repository) {
        this.repository = repository;
    }

    public boolean canTransition(QuestionStatus current, QuestionStatus target) {
        return TRANSICIONES_VALIDAS.getOrDefault(current, EnumSet.noneOf(QuestionStatus.class)).contains(target);
    }

    public QuestionState getCurrentState(Long questionId) {
        return repository.getOrCreate(questionId);
    }

    /**
     * Transición síncrona directa (usada por Review Service vía REST, o por
     * un administrador). Lanza IllegalStateException si la transición no es
     * válida desde el estado actual.
     */
    public QuestionState applyTransition(Long questionId, QuestionStatus target, String changedBy, String comment) {
        QuestionState state = repository.getOrCreate(questionId);
        if (!canTransition(state.getState(), target)) {
            throw new IllegalStateException(
                    "Transición inválida: no se puede pasar de " + state.getState() + " a " + target + ".");
        }
        state.transitionTo(target, changedBy, comment);
        return state;
    }

    /**
     * Procesamiento de una transición solicitada de forma ASÍNCRONA (comando
     * recibido desde RabbitMQ). A diferencia de applyTransition(), nunca
     * lanza excepción: registra el resultado (éxito o fallo) en OperationResult
     * para que el cliente pueda consultarlo posteriormente vía
     * GET /operations/{operationId}, cumpliendo el requisito de no informar
     * falsamente que una transición se completó.
     */
    public void processTransition(Long questionId, QuestionStatus target, String operationId) {
        OperationResult result = new OperationResult(operationId, questionId);
        try {
            applyTransition(questionId, target, "lifecycle-service (async)", "Procesado vía RabbitMQ");
            result.complete("Transición a " + target + " completada exitosamente.");
        } catch (IllegalStateException ex) {
            result.fail(ex.getMessage());
        } catch (Exception ex) {
            result.fail("Error inesperado al procesar la transición: " + ex.getMessage());
        }
        repository.saveOperation(result);
    }

    public OperationResult getOperationResult(String operationId) {
        return repository.findOperation(operationId).orElse(null);
    }
}
