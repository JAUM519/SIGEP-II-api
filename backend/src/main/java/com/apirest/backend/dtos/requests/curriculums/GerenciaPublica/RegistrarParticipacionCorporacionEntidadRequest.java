package com.apirest.backend.dtos.requests.curriculums.GerenciaPublica;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarParticipacionCorporacionEntidadRequest {
    @NotBlank
    @Size(max = 150)
    private String nombreCorporacion;
    @NotBlank
    @Size(max = 150)
    private String nombreRazonSocialInstitucion;
    @NotBlank
    @Size(max = 150)
    private String nombreEntidadOrganizacion;
}
