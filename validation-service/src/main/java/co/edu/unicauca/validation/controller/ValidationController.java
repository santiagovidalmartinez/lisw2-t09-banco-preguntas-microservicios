package co.edu.unicauca.validation.controller;

import co.edu.unicauca.validation.dto.ValidationRequestDTO;
import co.edu.unicauca.validation.dto.ValidationResultDTO;
import co.edu.unicauca.validation.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Comunicación síncrona (REST/HTTP): validación estructural de preguntas,
 * invocada por Question Service antes de persistir en estado DRAFT.
 */
@RestController
public class ValidationController {

    private final ValidationService validationService;

    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/validate")
    public ResponseEntity<ValidationResultDTO> validate(@RequestBody ValidationRequestDTO request) {
        ValidationResultDTO result = validationService.validate(request);
        if (!result.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        return ResponseEntity.ok(result);
    }
}
