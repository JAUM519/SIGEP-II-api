package com.apirest.backend.exceptions;

public class CurriculumAlreadyExistsException extends RuntimeException {
    public CurriculumAlreadyExistsException(String message) {
        super(message);
    }
}
