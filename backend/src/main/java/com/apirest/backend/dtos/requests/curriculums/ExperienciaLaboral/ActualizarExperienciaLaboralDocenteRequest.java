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
public class ActualizarExperienciaLaboralDocenteRequest {

    private String experienciaLaboralDocenteId;
    private Instant fechaTerminacion;
    @Max(1000000000)
    private Integer horasPromedioMes;
    @Size(max = 150)
    private String motivoRetiro;
    @Size(max = 150)
    private String telefono;
    @Size(max = 150)
    private String materiaImpartida;
    @Size(max = 150)
    private String certificadoLaboral;
    private Boolean documentoVerificado;
}
