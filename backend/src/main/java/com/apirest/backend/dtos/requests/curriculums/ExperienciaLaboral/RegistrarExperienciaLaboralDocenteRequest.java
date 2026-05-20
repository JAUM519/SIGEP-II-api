package com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral;

import com.apirest.backend.models.enums.Curriculum.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarExperienciaLaboralDocenteRequest {

    @NotNull
    private TipoInstitucionCurriculum tipoInstitucion;
    @NotBlank
    @Size(max = 150)
    private String nombreInstitucion;
    @NotBlank
    @Size(max = 150)
    private String pais;
    @NotBlank
    @Size(max = 150)
    private String departamento;
    @NotBlank
    @Size(max = 150)
    private String municipio;
    @NotNull
    private NivelAcademicoDocenteCurriculum nivelAcademico;
    @NotNull
    private AreaConocimientoCurriculum areaConocimiento;
    @NotNull
    private TipoZonaCurriculum tipoZona;
    @NotNull
    private Boolean trabajoActual;
    @NotNull
    @Past
    private Instant fechaIngreso;
    private Instant fechaTerminacion;
    @NotNull
    private JornadaLaboralCurriculum jornadaLaboral;
    @Max(1000000000)
    private Integer horasPromedioMes;
    @Size(max = 150)
    private String motivoRetiro;
    @Size(max = 150)
    private String telefono;
    @Size(max = 150)
    private String materiaImpartida;
    @Max(1000000000)
    @Min(1)
    private Integer tiempoExperiencia;
    @Size(max = 150)
    private String certificadoLaboral;
    private Boolean documentoVerificado;
}
