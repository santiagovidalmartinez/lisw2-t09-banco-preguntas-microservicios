package co.edu.unicauca.question.client;

import co.edu.unicauca.question.dto.ValidationRequestDTO;
import co.edu.unicauca.question.dto.ValidationResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Comunicación síncrona (REST): Question Service invoca a Validation Service
 * antes de persistir una pregunta. Maneja tanto el caso exitoso (200 OK) como
 * el de validación fallida (400 Bad Request con el detalle de errores) y el
 * caso de resiliencia en que Validation Service no esté disponible.
 */
@Component
public class ValidationClient {

    private final RestTemplate restTemplate;
    private final String validationServiceUrl;

    public ValidationClient(RestTemplate restTemplate,
                             @Value("${validation.service.url}") String validationServiceUrl) {
        this.restTemplate = restTemplate;
        this.validationServiceUrl = validationServiceUrl;
    }

    public ValidationResultDTO validate(ValidationRequestDTO request) {
        String url = validationServiceUrl + "/validate";
        try {
            ResponseEntity<ValidationResultDTO> response = restTemplate.postForEntity(url, request, ValidationResultDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException.BadRequest ex) {
            // Validation Service respondió 400 con el detalle de errores en el cuerpo.
            return ex.getResponseBodyAs(ValidationResultDTO.class);
        } catch (ResourceAccessException ex) {
            // Validation Service no está disponible: se informa como fallo de validación
            // controlado, sin exponer un error 500 crudo al cliente final (resiliencia).
            ValidationResultDTO fallback = new ValidationResultDTO();
            fallback.setValid(false);
            fallback.setErrors(java.util.List.of("No fue posible comunicarse con Validation Service: " + ex.getMessage()));
            return fallback;
        }
    }
}
