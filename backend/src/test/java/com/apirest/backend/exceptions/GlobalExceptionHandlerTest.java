package com.apirest.backend.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUserNotFoundException_ReturnsNotFound() {
        UserNotFoundException ex = new UserNotFoundException("123");
        ResponseEntity<String> response = handler.handleUserNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("El usuario con credencial: 123 no ha sido encontrado. ", response.getBody());
    }

    @Test
    void handleInvalidCredentialsException_ReturnsUnauthorized() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid");
        ResponseEntity<String> response = handler.handleInvalidCredentialsException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid", response.getBody());
    }

    @Test
    void handleCurriculumAlreadyExistsException_ReturnsConflict() {
        CurriculumAlreadyExistsException ex = new CurriculumAlreadyExistsException("Ya existe");
        ResponseEntity<String> response = handler.curriculumAlreadyExistsException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Ya existe", response.getBody());
    }

    @Test
    void handleCurriculumNotFoundException_ReturnsConflict() {
        CurriculumNotFoundException ex = new CurriculumNotFoundException("No encontrado");
        ResponseEntity<String> response = handler.curriculumNotFoundException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("No encontrado", response.getBody());
    }

    @Test
    void handleValidationException_ReturnsBadRequest() {
        // Creamos un mock de BindingResult que tiene un FieldError
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "mensaje de error");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<String> response = handler.handleValidationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("field: mensaje de error", response.getBody());
    }

    @Test
    void handleGeneralException_ReturnsInternalServerError() {
        Exception ex = new Exception("Ups");
        ResponseEntity<String> response = handler.handleGeneralException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Ocurrió un error inesperado", response.getBody());
    }

    @Test
    void handleAccessDeniedException_ReturnsForbidden() {
        AccessDeniedException ex = new AccessDeniedException("Acceso denegado");
        ResponseEntity<String> response = handler.handleAccessDeniedException(ex);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("No tienes permisos suficientes para realizar esta acción.", response.getBody());
    }

    @Test
    void handleBadCredentialsException_ReturnsUnauthorized() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");
        ResponseEntity<String> response = handler.handleBadCredentialsException(ex);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales de acceso inválidas.", response.getBody());
    }

    @Test
    void handleFileStorageException_ReturnsBadRequest() {
        FileStorageException ex = new FileStorageException("Error de archivo");
        ResponseEntity<String> response = handler.handleFileStorageException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de archivo", response.getBody());
    }

    @Test
    void handleInvalidFormat_ReturnsBadRequest() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Formato inválido");
        ResponseEntity<String> response = handler.handleInvalidFormat(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Formato inválido en la solicitud", response.getBody());
    }

}