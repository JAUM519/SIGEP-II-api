package com.apirest.backend.dtos.requests.curriculums.Educacion;

import com.apirest.backend.models.enums.Curriculum.IdiomaCurriculum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Normalized;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarIdiomaRequest {
    @NotBlank
    @Size(max = 150)
    private String idioma;
    @NotNull
    @Past
    private Instant fechaCertificado;
    @NotNull
    private IdiomaCurriculum conversacion;
    @NotNull
    private IdiomaCurriculum lectura;
    @NotNull
    private IdiomaCurriculum redaccion;
    @NotBlank
    @Size(max = 150)
    private String lenguaNativa;
    private Boolean certificado;
}
