package com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral;

import com.apirest.backend.models.enums.Curriculum.JornadaLaboralCurriculum;
import com.apirest.backend.models.enums.Curriculum.NivelJerarquicoEmpleoCurriculum;
import com.apirest.backend.models.enums.Curriculum.TipoEntidadCurriculum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarExperienciaLaboralRequest {

    @NotNull
    private TipoEntidadCurriculum tipoEntidad;
    @NotBlank
    @Size(max = 150)
    private String nombreEntidad;
    @NotBlank
    @Size(max = 150)
    private String pais;
    @NotBlank
    @Size(max = 150)
    private String departamento;
    @NotBlank
    @Size(max = 150)
    private String municipio;
    @NotBlank
    @Size(max = 150)
    private String direccionEntidad;
    @NotBlank
    @Size(max = 150)
    private String dependencia;
    @NotNull
    private NivelJerarquicoEmpleoCurriculum nivelJerarquicoEmpleo;
    @NotBlank
    @Size(max = 150)
    private String cargo;
    @Size(max = 150)
    private String telefono;
    @NotNull
    private Boolean trabajoActual;
    @NotNull
    @Past
    private Instant fechaIngreso;
    private Instant fechaRetiro;
    @NotNull
    private JornadaLaboralCurriculum jornadaLaboral;
    @Max(1000000000)
    private Integer horasPromedioMes;
    @Min(1)
    @Max(1000000000)
    private Integer tiempoExperiencia;
    @Size(max = 150)
    private String motivoRetiro;
    @Size(max = 150)
    private String certificadoLaboral;
    private Boolean documentoVerificado;
}
