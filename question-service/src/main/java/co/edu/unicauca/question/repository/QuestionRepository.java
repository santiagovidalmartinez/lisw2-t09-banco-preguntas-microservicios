package co.edu.unicauca.question.repository;

import co.edu.unicauca.question.model.Question;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Persistencia en memoria (Database per Service: este servicio no comparte tablas con los demás). */
@Repository
public class QuestionRepository {

    private final Map<Long, Question> store = new ConcurrentHashMap<>();

    public Question save(Question question) {
        store.put(question.getId(), question);
        return question;
    }

    public Optional<Question> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Iterable<Question> findAll() {
        return store.values();
    }
}
