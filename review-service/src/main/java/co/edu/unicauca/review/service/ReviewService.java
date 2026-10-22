package co.edu.unicauca.review.service;

import co.edu.unicauca.review.client.LifecycleClient;
import co.edu.unicauca.review.dto.QuestionStateDTO;
import co.edu.unicauca.review.dto.ReviewerAssignedEvent;
import co.edu.unicauca.review.messaging.EventPublisher;
import co.edu.unicauca.review.model.Review;
import co.edu.unicauca.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LifecycleClient lifecycleClient;
    private final EventPublisher eventPublisher;

    public ReviewService(ReviewRepository reviewRepository, LifecycleClient lifecycleClient, EventPublisher eventPublisher) {
        this.reviewRepository = reviewRepository;
        this.lifecycleClient = lifecycleClient;
        this.eventPublisher = eventPublisher;
    }

    public static class AssignmentResult {
        public boolean success;
        public String message;
        public Review review;
    }

    /**
     * Asigna un revisor a una pregunta elegible (debe estar en PENDING_REVIEW).
     * 1) Verifica elegibilidad consultando Lifecycle Service (síncrono).
     * 2) Registra la asignación localmente.
     * 3) Solicita la transición PENDING_REVIEW -> IN_REVIEW (síncrono).
     * 4) Solo si la transición fue exitosa, publica el evento ReviewerAssigned
     *    (asíncrono) y confirma la asignación; si falla, revierte el registro
     *    local para evitar inconsistencias entre asignación y estado.
     */
    public AssignmentResult assignReviewer(Long questionId, Long reviewerId) {
        AssignmentResult result = new AssignmentResult();

        QuestionStateDTO estadoActual = lifecycleClient.getState(questionId);
        if (estadoActual == null || !"PENDING_REVIEW".equals(estadoActual.getState())) {
            result.success = false;
            result.message = "La pregunta no es elegible para asignación de revisor (estado actual: "
                    + (estadoActual != null ? estadoActual.getState() : "DESCONOCIDO") + ").";
            return result;
        }

        Review review = new Review(questionId, reviewerId);
        reviewRepository.save(review);

        boolean transicionOk = lifecycleClient.requestTransition(questionId, "IN_REVIEW", "review-service", "Revisor asignado: " + reviewerId);

        if (!transicionOk) {
            // Revertir para evitar inconsistencia entre la asignación y el estado real.
            review.resolve(Review.ReviewStatus.ASSIGNED, "ROLLBACK: la transición IN_REVIEW fue rechazada por Lifecycle Service.");
            result.success = false;
            result.message = "No fue posible transicionar la pregunta a IN_REVIEW; la asignación no se confirma.";
            result.review = review;
            return result;
        }

        eventPublisher.publishReviewerAssigned(new ReviewerAssignedEvent(questionId, reviewerId));

        result.success = true;
        result.message = "Revisor asignado correctamente y transición a IN_REVIEW confirmada.";
        result.review = review;
        return result;
    }

    public Review decide(Long reviewId, String decision, String comments) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new java.util.NoSuchElementException("Revisión no encontrada: " + reviewId));

        Review.ReviewStatus nuevoEstado = "APPROVED".equalsIgnoreCase(decision)
                ? Review.ReviewStatus.APPROVED : Review.ReviewStatus.REJECTED;

        boolean transicionOk = lifecycleClient.requestTransition(
                review.getQuestionId(), nuevoEstado.name(), "review-service", comments);

        if (!transicionOk) {
            throw new IllegalStateException("Lifecycle Service rechazó la transición a " + nuevoEstado + ".");
        }

        review.resolve(nuevoEstado, comments);
        return review;
    }
}
