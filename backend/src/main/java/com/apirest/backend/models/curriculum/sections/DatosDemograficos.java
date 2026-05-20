package com.apirest.backend.models.curriculum.sections;

import com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum;
import com.apirest.backend.models.enums.Curriculum.PreferenciaEtnicaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatosDemograficos {
    private String nacionalidad;
    private EstadoCivilCurriculum estadoCivil;
    private PreferenciaEtnicaCurriculum preferenciaEtnica;
    private String paisNacimiento;
    private String departamentoNacimiento;
    private String municipioNacimiento;
    private Boolean discapacidad;
}
