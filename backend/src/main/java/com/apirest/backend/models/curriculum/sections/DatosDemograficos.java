package com.apirest.backend.models.curriculum.sections;

import com.apirest.backend.models.enums.EstadoCivilCurriculum;
import com.apirest.backend.models.enums.PreferenciaEtnicaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private boolean discapacidad;
}
