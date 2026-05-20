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
    private ClaseLibretaMilitarCurriculum claseLibretaMilitar;
    @Size(max = 150)
    private String numeroLibretaMilitar;
    @Max(1000000000)
    private Integer distritoMilitar;
    @Size(max = 150)
    private String documentoIdentificacion;
    private Boolean documentoVerificado;
    @Size(max = 150)
    private String libretaMilitar;
    private Boolean libretaVerificada;
    private Boolean personaExpuestaPoliticamente;
}
