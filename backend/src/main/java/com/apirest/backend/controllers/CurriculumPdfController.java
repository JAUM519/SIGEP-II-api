package com.apirest.backend.controllers;

import com.apirest.backend.services.CurriculumPdfService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumPdfController {

    private final CurriculumPdfService curriculumPdfService;

    public CurriculumPdfController(CurriculumPdfService curriculumPdfService) {
        this.curriculumPdfService = curriculumPdfService;
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarHojaVida(Authentication authentication) {
        String usuarioId = obtenerUsuarioId(authentication);
        byte[] pdf = curriculumPdfService.generarHojaVida(usuarioId);

        ContentDisposition contentDisposition = ContentDisposition
                .attachment()
                .filename("hoja-de-vida.pdf", StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    private String obtenerUsuarioId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No se pudo identificar al usuario autenticado.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String usuarioId && !usuarioId.isBlank()) {
            return usuarioId;
        }

        try {
            Method getId = principal.getClass().getMethod("getId");
            Object id = getId.invoke(principal);
            if (id != null && !id.toString().isBlank()) {
                return id.toString();
            }
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("No se pudo obtener el identificador del usuario autenticado.", ex);
        }

        throw new IllegalStateException("No se pudo obtener el identificador del usuario autenticado.");
    }
}
