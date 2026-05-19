package com.apirest.backend.dtos.requests.curriculums.Educacion;

import com.apirest.backend.models.enums.Curriculum.AreaConocimientoCurriculum;
import com.apirest.backend.models.enums.Curriculum.EstadoEstudioCurriculum;
import com.apirest.backend.models.enums.Curriculum.NivelAcademicoCurriculum;
import com.apirest.backend.models.enums.Curriculum.NivelFormacionCurriculum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarFormacionAcademicaRequest {

    private String formacionId;
    private AreaConocimientoCurriculum areaConocimiento;
    @Size(max=150)
    private String programaAcademico;
    @Max(12)
    private Integer semestresAprobados;
    private EstadoEstudioCurriculum estadoEstudio;
    private Instant fechaTerminacionMaterias;
    private Instant fechaGrado;
    private Boolean estudioConvalidado;
    private Instant fechaConvalidacion;
    @Size(max=150)
    private String tarjetaProfesional;
    private Instant estudioExterior;
    @Size(max=150)
    private String archivoTarjetaProfesioal;
    private Boolean verificTarjetaProfesional;
    @Size(max=150)
    private String archivoEducacionFormal;
    private Boolean verificEducacionFormal;
}
