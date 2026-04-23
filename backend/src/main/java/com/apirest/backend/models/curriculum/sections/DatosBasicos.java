package com.apirest.backend.models.curriculum.sections;

import com.apirest.backend.models.enums.ClaseLibretaMilitarCurriculum;
import com.apirest.backend.models.enums.GeneroCurriculum;
import com.apirest.backend.models.enums.TipoIdentificacionUsuarios;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatosBasicos {
    private String nombre;
    private TipoIdentificacionUsuarios tipoIdentificacion;
    private String numeroIdentificacion;
    private Instant fechaNacimiento;
    private String email;
    private GeneroCurriculum genero;
    private ClaseLibretaMilitarCurriculum claseLibretaMilitar;
    private String numeroLibretaMilitar;
    private Integer distritoMilitar;
    private String documentoIdentificado;
    private boolean documentoVerificado;
    private String libretaMilitar;
    private boolean libretaVerificada;
    private boolean personaExpuestaPoliticamente;
}
