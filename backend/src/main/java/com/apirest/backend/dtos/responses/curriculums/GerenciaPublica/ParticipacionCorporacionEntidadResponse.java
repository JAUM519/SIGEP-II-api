package com.apirest.backend.dtos.responses.curriculums.GerenciaPublica;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipacionCorporacionEntidadResponse {
    private String id;
    private String nombreCorporacion;
    private String nombreRazonSocialInstitucion;
    private String nombreEntidadOrganizacion;
}
