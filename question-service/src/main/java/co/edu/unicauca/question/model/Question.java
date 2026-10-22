package co.edu.unicauca.question.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Question {

    private static final AtomicLong SEQUENCE = new AtomicLong(1000);

    private final Long id;
    private final String enunciado;
    private final List<String> opciones;
    private final Integer correctOptionIndex;
    private final String clasificacion;
    private final String autorId;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Question(String enunciado, List<String> opciones, Integer correctOptionIndex, String clasificacion, String autorId) {
        this.id = SEQUENCE.incrementAndGet();
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.correctOptionIndex = correctOptionIndex;
        this.clasificacion = clasificacion;
        this.autorId = autorId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Long getId() { return id; }
    public String getEnunciado() { return enunciado; }
    public List<String> getOpciones() { return opciones; }
    public Integer getCorrectOptionIndex() { return correctOptionIndex; }
    public String getClasificacion() { return clasificacion; }
    public String getAutorId() { return autorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
