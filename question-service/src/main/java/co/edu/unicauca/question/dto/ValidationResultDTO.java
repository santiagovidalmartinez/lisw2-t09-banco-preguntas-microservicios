package co.edu.unicauca.question.dto;

import java.util.List;

public class ValidationResultDTO {

    private boolean valid;
    private List<String> errors;

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
