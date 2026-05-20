package com.apirest.backend.dtos.responses.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.ClaseLibretaMilitarCurriculum;
import com.apirest.backend.models.enums.Curriculum.GeneroCurriculum;
import com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatosBasicosResponse {
    private String nombre;
    private TipoIdentificacionUsuarios tipoIdentificacion;
    private String numeroIdentificacion;
    private Instant fechaNacimiento;
    private String email;
    private GeneroCurriculum genero;
    private ClaseLibretaMilitarCurriculum claseLibretaMilitar;
    private String numeroLibretaMilitar;
    private Integer distritoMilitar;
    private String documentoIdentificacion;
    private Boolean documentoVerificado;
    private String libretaMilitar;
    private Boolean libretaVerificada;
    private Boolean personaExpuestaPoliticamente;
}
