package com.apirest.backend.dtos.requests.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.EstadoCivilCurriculum;
import com.apirest.backend.models.enums.Curriculum.PreferenciaEtnicaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarDatosDemograficosRequest {

    private EstadoCivilCurriculum estadoCivil;
    private PreferenciaEtnicaCurriculum preferenciaEtnica;
    private Boolean discapacidad;
}
