package co.edu.unicauca.question.dto;

import java.util.List;

/** DTO espejo del contrato esperado por Validation Service (POST /validate). */
public class ValidationRequestDTO {

    private String enunciado;
    private List<String> opciones;
    private Integer correctOptionIndex;

    public ValidationRequestDTO() {}

    public ValidationRequestDTO(String enunciado, List<String> opciones, Integer correctOptionIndex) {
        this.enunciado = enunciado;
        this.opciones = opciones;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }
    public Integer getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(Integer correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
}
