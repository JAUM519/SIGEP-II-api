package com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarExperienciaLaboralRequest {

    private String experienciaLaboralId;
    @Size(max = 150)
    private String telefono;
    private Instant fechaRetiro;
    @Max(1000000000)
    private Integer horasPromedioMes;
    @Size(max = 150)
    private String motivoRetiro;
    @Size(max = 150)
    private String certificadoLaboral;
    private Boolean documentoVerificado;
}
