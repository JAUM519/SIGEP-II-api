package com.apirest.backend.dtos.requests.curriculums.GerenciaPublica;

import com.apirest.backend.models.enums.Curriculum.ArticuloCurriculum;
import com.apirest.backend.models.enums.Curriculum.LibroResultadoInvestigacionCurriculum;
import com.apirest.backend.models.enums.Curriculum.TiposProduccionBibliograficaCurriculum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarPublicacionRequest {
    @NotNull
    private ArticuloCurriculum articulo;
    @NotBlank
    @Size(max = 150)
    private String nombreArticulo;
    @NotNull
    private LibroResultadoInvestigacionCurriculum libroResultadoInvestigacion;
    @NotBlank
    @Size(max = 150)
    private String nombreLibroRevista;
    @NotNull
    private TiposProduccionBibliograficaCurriculum tiposProduccionBibliografica;
    @NotBlank
    @Size(max = 150)
    private String nombrePublicacion;
}
