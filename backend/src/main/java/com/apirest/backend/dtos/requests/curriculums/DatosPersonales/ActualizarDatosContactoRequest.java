package com.apirest.backend.dtos.requests.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.ZonaCurriculum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarDatosContactoRequest {
    @Size(max = 150)
    private String paisResidencia;
    @Size(max = 150)
    private String departamentoResidencia;
    @Size(max = 150)
    private String municipioResidencia;
    private ZonaCurriculum zona;
    @Size(max = 150)
    private String direccionResidencia;
    @Size(max = 150)
    private String telefonoResidencia;
    @Size(max = 150)
    private String celular;
    @Size(max = 150)
    private String telefonoOficina;
    @Size(max = 150)
    private String extension;
    @Email
    @Size(max = 150)
    private String emailPersonalPrincipal;
    @Email
    @Size(max = 150)
    private String emailOficina;
}
