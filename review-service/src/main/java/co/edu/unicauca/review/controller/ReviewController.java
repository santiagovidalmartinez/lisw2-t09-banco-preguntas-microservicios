package co.edu.unicauca.review.controller;

import co.edu.unicauca.review.dto.AssignReviewerRequestDTO;
import co.edu.unicauca.review.dto.ReviewDecisionRequestDTO;
import co.edu.unicauca.review.model.Review;
import co.edu.unicauca.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assign(@RequestBody AssignReviewerRequestDTO request) {
        ReviewService.AssignmentResult result = reviewService.assignReviewer(request.getQuestionId(), request.getReviewerId());

        if (!result.success) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", result.message));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", result.message,
                "reviewId", result.review.getId(),
                "questionId", result.review.getQuestionId(),
                "reviewerId", result.review.getReviewerId()
        ));
    }

    @PostMapping("/{id}/decision")
    public ResponseEntity<?> decide(@PathVariable Long id, @RequestBody ReviewDecisionRequestDTO request) {
        try {
            Review review = reviewService.decide(id, request.getDecision(), request.getComments());
            return ResponseEntity.ok(Map.of(
                    "reviewId", review.getId(),
                    "status", review.getStatus(),
                    "comments", review.getComments() == null ? "" : review.getComments()
            ));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
        }
    }
}
