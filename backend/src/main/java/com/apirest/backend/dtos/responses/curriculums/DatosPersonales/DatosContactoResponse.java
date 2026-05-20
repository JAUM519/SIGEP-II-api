package com.apirest.backend.dtos.responses.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.ZonaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatosContactoResponse {
    private String paisResidencia;
    private String departamentoResidencia;
    private String municipioResidencia;
    private ZonaCurriculum zona;
    private String direccionResidencia;
    private String telefonoResidencia;
    private String celular;
    private String telefonoOficina;
    private String extension;
    private String emailPersonalPrincipal;
    private String emailOficina;
}
