package com.lovablepetro.chamadopetro.dto;

import java.util.List;
import java.util.ArrayList;

public class ImportResultDTO {
    private int success;
    private List<String> errors;
    private List<String> duplicates;
    
    public ImportResultDTO() {
        this.success = 0;
        this.errors = new ArrayList<>();
        this.duplicates = new ArrayList<>();
    }
    
    public ImportResultDTO(int success, List<String> errors, List<String> duplicates) {
        this.success = success;
        this.errors = errors != null ? errors : new ArrayList<>();
        this.duplicates = duplicates != null ? duplicates : new ArrayList<>();
    }
    
    // Getters and Setters
    public int getSuccess() {
        return success;
    }
    
    public void setSuccess(int success) {
        this.success = success;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public List<String> getDuplicates() {
        return duplicates;
    }
    
    public void setDuplicates(List<String> duplicates) {
        this.duplicates = duplicates;
    }
    
    // Helper methods
    public void addError(String error) {
        this.errors.add(error);
    }
    
    public void addDuplicate(String duplicate) {
        this.duplicates.add(duplicate);
    }
    
    public void incrementSuccess() {
        this.success++;
    }
}