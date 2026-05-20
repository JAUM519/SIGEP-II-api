package com.apirest.backend.dtos.requests.curriculums.GerenciaPublica;

import com.apirest.backend.models.enums.Curriculum.TipoPremioReconocimientoCurriculum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarPremioReconocimientoRequest {
    @NotNull
    private TipoPremioReconocimientoCurriculum tipo;
    @NotBlank
    @Size(max = 150)
    private String nombreEntidadOrganizacion;
    @NotNull
    @PastOrPresent
    private Instant fecha;
    @NotBlank
    @Size(max = 150)
    private String pais;
    @NotBlank
    @Size(max = 150)
    private String departamento;
    @NotBlank
    @Size(max = 150)
    private String municipio;
}
