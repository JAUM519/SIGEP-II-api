package com.apirest.backend.dtos.requests.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.ClaseLibretaMilitarCurriculum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarDatosBasicosRequest {
    @Size(max = 150)
    private ClaseLibretaMilitarCurriculum claseLibretaMilitar;
    @Size(max = 150)
    private String numeroLibretaMilitar;
    @Size(max = 150)
    private Integer distritoMilitar;
    @Size(max = 150)
    private String libretaMilitar;
    @Size(max = 150)
    private Boolean libretaVerificada;
    @Size(max = 150)
    private Boolean personaExpuestaPoliticamente;
}
