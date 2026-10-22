package co.edu.unicauca.lifecycle.controller;

import co.edu.unicauca.lifecycle.dto.TransitionRequestDTO;
import co.edu.unicauca.lifecycle.model.OperationResult;
import co.edu.unicauca.lifecycle.model.QuestionState;
import co.edu.unicauca.lifecycle.model.QuestionStatus;
import co.edu.unicauca.lifecycle.service.LifecycleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LifecycleController {

    private final LifecycleService lifecycleService;

    public LifecycleController(LifecycleService lifecycleService) {
        this.lifecycleService = lifecycleService;
    }

    /** Consulta síncrona del estado actual de una pregunta (usada por Review Service). */
    @GetMapping("/lifecycle/{questionId}")
    public ResponseEntity<QuestionState> getState(@PathVariable Long questionId) {
        return ResponseEntity.ok(lifecycleService.getCurrentState(questionId));
    }

    /**
     * Transición síncrona directa, solicitada por otro servicio o por un
     * administrador (por ejemplo, Review Service pidiendo PENDING_REVIEW -> IN_REVIEW,
     * o un revisor aprobando/rechazando IN_REVIEW -> APPROVED/REJECTED).
     */
    @PostMapping("/lifecycle/{questionId}/transition")
    public ResponseEntity<?> transition(@PathVariable Long questionId, @RequestBody TransitionRequestDTO request) {
        QuestionStatus target;
        try {
            target = QuestionStatus.valueOf(request.getTargetState());
        } catch (IllegalArgumentException | NullPointerException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", "Estado destino inválido: " + request.getTargetState()));
        }

        try {
            QuestionState nuevoEstado = lifecycleService.applyTransition(
                    questionId, target, request.getChangedBy(), request.getComment());
            return ResponseEntity.ok(nuevoEstado);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
        }
    }

    /**
     * Permite al cliente consultar el resultado de una transición asíncrona
     * (comando procesado vía RabbitMQ), identificada por su operationId.
     */
    @GetMapping("/operations/{operationId}")
    public ResponseEntity<?> getOperation(@PathVariable String operationId) {
        OperationResult result = lifecycleService.getOperationResult(operationId);
        if (result == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                    "operationId", operationId,
                    "status", "PENDING",
                    "message", "La operación aún no ha sido procesada por Lifecycle Service."
            ));
        }
        return ResponseEntity.ok(result);
    }
}
