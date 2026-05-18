package com.apirest.backend.dtos.requests.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.ZonaCurriculum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarDatosContactoRequest {
    @NotBlank
    @Size(max = 150)
    private String paisResidencia;
    @NotBlank
    @Size(max = 150)
    private String departamentoResidencia;
    @NotBlank
    @Size(max = 150)
    private String municipioResidencia;
    @NotNull
    private ZonaCurriculum zona;
    @NotBlank
    @Size(max = 150)
    private String direccionResidencia;
    @Size(max = 150)
    private String telefonoResidencia;
    @NotBlank
    @Size(max = 150)
    private String celular;
    @Size(max = 150)
    private String telefonoOficina;
    @Size(max = 150)
    private String extension;
    @NotBlank
    @Email
    @Size(max = 150)
    private String emailPersonalPrincipal;
    @Email
    @Size(max = 150)
    private String emailOficina;
}
