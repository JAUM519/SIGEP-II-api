package com.apirest.backend.dtos.responses.curriculums.GerenciaPublica;

import com.apirest.backend.models.enums.Curriculum.ArticuloCurriculum;
import com.apirest.backend.models.enums.Curriculum.LibroResultadoInvestigacionCurriculum;
import com.apirest.backend.models.enums.Curriculum.TiposProduccionBibliograficaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionResponse {
    private String id;
    private ArticuloCurriculum articulo;
    private String nombreArticulo;
    private LibroResultadoInvestigacionCurriculum libroResultadoInvestigacion;
    private String nombreLibroRevista;
    private TiposProduccionBibliograficaCurriculum tiposProduccionBibliografica;
    private String nombrePublicacion;
}
