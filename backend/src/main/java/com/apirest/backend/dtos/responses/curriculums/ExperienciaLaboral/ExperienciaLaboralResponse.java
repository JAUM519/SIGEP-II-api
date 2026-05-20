package com.apirest.backend.dtos.responses.curriculums.ExperienciaLaboral;

import com.apirest.backend.models.enums.Curriculum.JornadaLaboralCurriculum;
import com.apirest.backend.models.enums.Curriculum.NivelJerarquicoEmpleoCurriculum;
import com.apirest.backend.models.enums.Curriculum.TipoEntidadCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienciaLaboralResponse {
    private String id;
    private TipoEntidadCurriculum tipoEntidad;
    private String nombreEntidad;
    private String pais;
    private String departamento;
    private String municipio;
    private String direccionEntidad;
    private String dependencia;
    private NivelJerarquicoEmpleoCurriculum nivelJerarquiaEmpleo;
    private String cargo;
    private String telefono;
    private Boolean trabajoActual;
    private Instant fechaIngreso;
    private Instant fechaRetiro;
    private JornadaLaboralCurriculum jornadaLaboral;
    private Integer horasPromedioMes;
    private Integer tiempoExperiencia;
    private String motivoRetiro;
    private String certificadoLaboral;
    private Boolean documentoVerificado;
}
