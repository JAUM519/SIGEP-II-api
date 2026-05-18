package com.apirest.backend.models.curriculum.sections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipacionCorporacionEntidad {
    @Id
    private String id;
    private String nombreCorporacion;
    private String nombreRazonSocialInstitucion;
    private String nombreEntidadOrganizacion;
}
