package co.edu.unicauca.validation.service;

import co.edu.unicauca.validation.dto.ValidationRequestDTO;
import co.edu.unicauca.validation.dto.ValidationResultDTO;
import org.springframework.stereotype.Service;

/**
 * Reglas de validación estructural para una pregunta de selección múltiple
 * con única respuesta: enunciado no vacío, exactamente 4 opciones no vacías
 * y un índice de respuesta correcta válido (0-3).
 */
@Service
public class ValidationService {

    public ValidationResultDTO validate(ValidationRequestDTO request) {
        ValidationResultDTO result = new ValidationResultDTO();

        if (request.getEnunciado() == null || request.getEnunciado().isBlank()) {
            result.addError("El enunciado de la pregunta es obligatorio.");
        }

        if (request.getOpciones() == null || request.getOpciones().size() != 4) {
            result.addError("La pregunta debe tener exactamente 4 opciones de respuesta.");
        } else {
            for (int i = 0; i < request.getOpciones().size(); i++) {
                String opcion = request.getOpciones().get(i);
                if (opcion == null || opcion.isBlank()) {
                    result.addError("La opción " + (i + 1) + " no puede estar vacía.");
                }
            }
        }

        Integer correctIndex = request.getCorrectOptionIndex();
        if (correctIndex == null) {
            result.addError("Debe indicarse el índice de la opción correcta.");
        } else if (request.getOpciones() != null && (correctIndex < 0 || correctIndex >= request.getOpciones().size())) {
            result.addError("El índice de la opción correcta está fuera de rango (debe ser 0 a 3).");
        }

        result.setValid(!result.hasErrors());
        return result;
    }
}
