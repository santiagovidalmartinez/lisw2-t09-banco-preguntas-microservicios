package co.edu.unicauca.question.controller;

import co.edu.unicauca.question.client.ValidationClient;
import co.edu.unicauca.question.dto.CreateQuestionRequestDTO;
import co.edu.unicauca.question.dto.SubmitQuestionCommand;
import co.edu.unicauca.question.dto.ValidationRequestDTO;
import co.edu.unicauca.question.dto.ValidationResultDTO;
import co.edu.unicauca.question.messaging.CommandPublisher;
import co.edu.unicauca.question.model.Question;
import co.edu.unicauca.question.repository.QuestionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionRepository questionRepository;
    private final ValidationClient validationClient;
    private final CommandPublisher commandPublisher;

    public QuestionController(QuestionRepository questionRepository,
                               ValidationClient validationClient,
                               CommandPublisher commandPublisher) {
        this.questionRepository = questionRepository;
        this.validationClient = validationClient;
        this.commandPublisher = commandPublisher;
    }

    /**
     * Comunicación síncrona: crea una pregunta solo si Validation Service
     * la aprueba. Exige el encabezado Authorization (ver nota sobre
     * simulación de token en el README/informe: no se implementa JWT real
     * en esta versión, tal como lo indica el enunciado del taller).
     */
    @PostMapping
    public ResponseEntity<?> createQuestion(@RequestBody CreateQuestionRequestDTO request,
                                             @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || authorization.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Falta el encabezado Authorization con el token de sesión."));
        }

        ValidationResultDTO validationResult = validationClient.validate(
                new ValidationRequestDTO(request.getEnunciado(), request.getOpciones(), request.getCorrectOptionIndex()));

        if (!validationResult.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "message", "La pregunta no superó la validación estructural.",
                    "errors", validationResult.getErrors()
            ));
        }

        Question pregunta = new Question(
                request.getEnunciado(), request.getOpciones(), request.getCorrectOptionIndex(),
                request.getClasificacion(), authorization);
        questionRepository.save(pregunta);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", pregunta.getId(),
                "status", "DRAFT",
                "message", "Pregunta creada y persistida en estado DRAFT."
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable Long id) {
        return questionRepository.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<?> listQuestions() {
        return ResponseEntity.ok(questionRepository.findAll());
    }

    /**
     * Comunicación asíncrona: solicita enviar la pregunta a revisión.
     * Publica el comando en RabbitMQ y responde de inmediato con
     * 202 Accepted, sin esperar a que Lifecycle Service procese la
     * transición DRAFT -> PENDING_REVIEW.
     */
    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitForReview(@PathVariable Long id) {
        try {
            questionRepository.findById(id).orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }

        String operationId = UUID.randomUUID().toString();
        SubmitQuestionCommand command = new SubmitQuestionCommand(id, operationId, "docente1");
        commandPublisher.publishSubmitForReview(command);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "message", "Solicitud de envío a revisión aceptada para procesamiento.",
                "operationId", operationId,
                "status", "PROCESSING"
        ));
    }
}
