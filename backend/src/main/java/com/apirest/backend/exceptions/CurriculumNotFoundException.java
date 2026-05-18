package com.apirest.backend.exceptions;

public class CurriculumNotFoundException extends RuntimeException {
    public CurriculumNotFoundException(String message) {
        super(message);
    }
}
