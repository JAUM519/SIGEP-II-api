package com.apirest.backend.dtos.requests.curriculums.Educacion;

import com.apirest.backend.models.enums.Curriculum.MedioCapacitacionCurriculum;
import com.apirest.backend.models.enums.Curriculum.ModalidadCurriculum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarEducacionTrabajoRequest {
    @NotNull
    @Past
    private Instant fechaFinalizacion;
    @Min(1)
    @Max(1000000000)
    private Integer numeroTotalHoras;
    @NotBlank
    @Size(max=150)
    private String pais;
    @NotBlank
    @Size(max=150)
    private String nombre;
    @NotBlank
    @Size(max=150)
    private String institucion;
    @NotNull
    private MedioCapacitacionCurriculum medioCapacitacion;
    @NotNull
    private ModalidadCurriculum modalidad;
    @NotBlank
    @Size(max=150)
    private String diplomaActaCertificadoEstudio;
}
