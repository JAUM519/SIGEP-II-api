package com.apirest.backend.dtos.requests.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum;
import com.apirest.backend.models.enums.Curriculum.PreferenciaEtnicaCurriculum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarDatosDemograficosRequest {
    @NotBlank
    @Size(max = 150)
    private String nacionalidad;
    @NotNull
    private EstadoCivilCurriculum estadoCivil;
    @NotNull
    private PreferenciaEtnicaCurriculum preferenciaEtnica;
    @NotBlank
    @Size(max = 150)
    private String paisNacimiento;
    @NotBlank
    @Size(max = 150)
    private String departamentoNacimiento;
    @NotBlank
    @Size(max = 150)
    private String municipioNacimiento;
    private Boolean discapacidad;
}
