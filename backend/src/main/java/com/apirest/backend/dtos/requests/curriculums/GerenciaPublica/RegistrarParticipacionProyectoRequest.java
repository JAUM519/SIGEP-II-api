package com.apirest.backend.dtos.requests.curriculums.GerenciaPublica;

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
public class RegistrarParticipacionProyectoRequest {
    @NotBlank
    @Size(max = 150)
    private String nombre;
    @NotBlank
    @Size(max = 150)
    private String rolDesempeñado;
    @NotBlank
    @Size(max = 150)
    private String nombreEntidadOrganizacion;
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
    @PastOrPresent
    private Instant fechaInicio;
    @NotNull
    private Instant fechaTerminacion;
}
