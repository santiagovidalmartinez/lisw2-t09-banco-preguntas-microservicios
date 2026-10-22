package co.edu.unicauca.validation.dto;

import java.util.List;

public class ValidationRequestDTO {

    private String enunciado;
    private List<String> opciones;
    private Integer correctOptionIndex;

    public ValidationRequestDTO() {}

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }
    public Integer getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(Integer correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
}
