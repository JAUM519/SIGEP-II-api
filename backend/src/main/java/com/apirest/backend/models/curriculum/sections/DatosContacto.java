package com.apirest.backend.models.curriculum.sections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DatosContacto {
    private String paisResidencia;
    private String departamentoResidencia;
    private String municipioResidencia;
    private String zona;
    private String direccionResidencia;
    private String telefonoResidencia;
    private String celular;
    private String telefonoOficina;
    private String extension;
    private String emailPersonalPrincipal;
    private String emailOficina;
}
