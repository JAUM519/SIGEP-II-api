package com.apirest.backend.dtos.responses.curriculums.Educacion;

import com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdiomaResponse {
    private String id;
    private String idioma;
    private Instant fechaCertificado;
    private IdiomaCurriculum conversacion;
    private IdiomaCurriculum lectura;
    private IdiomaCurriculum redaccion;
    private Boolean lenguaNativa;
    private String certificado;
}
