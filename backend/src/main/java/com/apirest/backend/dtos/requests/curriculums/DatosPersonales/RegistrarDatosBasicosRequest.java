package com.apirest.backend.dtos.requests.curriculums.DatosPersonales;

import com.apirest.backend.models.enums.Curriculum.ClaseLibretaMilitarCurriculum;
import com.apirest.backend.models.enums.Curriculum.GeneroCurriculum;
import com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarDatosBasicosRequest {
    @NotBlank
    @Size(max = 150)
    private String nombre;
    @NotNull
    private TipoIdentificacionUsuarios tipoIdentificacion;
    @NotBlank
    @Size(max = 150)
    private String numeroIdentificacion;
    @NotNull
    @PastOrPresent
    private Instant fechaNacimiento;
    @NotBlank
    @Email
    @Size(max = 150)
    private String email;
    @NotNull
    private GeneroCurriculum genero;
    private Boolean tieneLibretaMilitar;
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
