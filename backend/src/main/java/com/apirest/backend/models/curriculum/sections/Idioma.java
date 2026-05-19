package com.apirest.backend.models.curriculum.sections;

import com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Idioma {
    @Id
    private String id;
    private String idioma;
    private Instant fechaCertificado;
    private IdiomaCurriculum conversacion;
    private IdiomaCurriculum lectura;
    private IdiomaCurriculum redaccion;
    private Boolean lenguaNativa;
    private String certificado;
}
