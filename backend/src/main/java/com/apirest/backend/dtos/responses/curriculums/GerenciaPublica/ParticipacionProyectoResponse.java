package com.apirest.backend.dtos.responses.curriculums.GerenciaPublica;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipacionProyectoResponse {
    private String id;
    private String nombre;
    private String rolDesempeñado;
    private String nombreEntidadOrganizacion;
    private String pais;
    private String departamento;
    private String municipio;
    private Instant fechaInicio;
    private Instant fechaTerminacion;
}
