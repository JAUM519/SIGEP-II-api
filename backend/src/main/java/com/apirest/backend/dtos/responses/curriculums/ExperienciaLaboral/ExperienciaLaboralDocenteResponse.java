package com.apirest.backend.dtos.responses.curriculums.ExperienciaLaboral;

import com.apirest.backend.models.enums.Curriculum.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienciaLaboralDocenteResponse {
    private String id;
    private TipoInstitucionCurriculum tipoInstitucion;
    private String nombreInstitucion;
    private String pais;
    private String departamento;
    private String municipio;
    private NivelAcademicoDocenteCurriculum nivelAcademico;
    private AreaConocimientoCurriculum areaConocimiento;
    private TipoZonaCurriculum tipoZona;
    private Boolean trabajoActual;
    private Instant fechaIngreso;
    private Instant fechaTerminacion;
    private JornadaLaboralCurriculum jornadaLaboral;
    private Integer horasPromedioMes;
    private String motivoRetiro;
    private String telefono;
    private String materiaImpartida;
    private Integer tiempoExperiencia;
    private String certificadoLaboral;
    private Boolean documentoVerificado;
}
