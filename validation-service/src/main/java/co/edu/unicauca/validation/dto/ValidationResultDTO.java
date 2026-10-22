package co.edu.unicauca.validation.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationResultDTO {

    private boolean valid;
    private List<String> errors = new ArrayList<>();

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }
    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
