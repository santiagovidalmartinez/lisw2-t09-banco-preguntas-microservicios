package co.edu.unicauca.review.client;

import co.edu.unicauca.review.dto.QuestionStateDTO;
import co.edu.unicauca.review.dto.TransitionRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Comunicación síncrona (REST): Review Service consulta y solicita
 * transiciones de estado al Lifecycle Service, que es la única autoridad
 * sobre el ciclo de vida de la pregunta.
 */
@Component
public class LifecycleClient {

    private final RestTemplate restTemplate;
    private final String lifecycleServiceUrl;

    public LifecycleClient(RestTemplate restTemplate, @Value("${lifecycle.service.url}") String lifecycleServiceUrl) {
        this.restTemplate = restTemplate;
        this.lifecycleServiceUrl = lifecycleServiceUrl;
    }

    public QuestionStateDTO getState(Long questionId) {
        return restTemplate.getForObject(lifecycleServiceUrl + "/lifecycle/" + questionId, QuestionStateDTO.class);
    }

    /** @return true si la transición fue aceptada; false si Lifecycle Service la rechazó (409 Conflict). */
    public boolean requestTransition(Long questionId, String targetState, String changedBy, String comment) {
        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(
                    lifecycleServiceUrl + "/lifecycle/" + questionId + "/transition",
                    new TransitionRequestDTO(targetState, changedBy, comment),
                    Object.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException.Conflict ex) {
            return false;
        }
    }
}
