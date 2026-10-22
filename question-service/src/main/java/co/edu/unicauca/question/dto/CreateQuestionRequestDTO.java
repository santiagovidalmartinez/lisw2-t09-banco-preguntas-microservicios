package co.edu.unicauca.question.dto;

import java.util.List;

public class CreateQuestionRequestDTO {

    private String enunciado;
    private List<String> opciones;
    private Integer correctOptionIndex;
    private String clasificacion;

    public CreateQuestionRequestDTO() {}

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public List<String> getOpciones() { return opciones; }
    public void setOpciones(List<String> opciones) { this.opciones = opciones; }
    public Integer getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(Integer correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
}
