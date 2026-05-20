package com.apirest.backend.models.curriculum.sections;

import com.apirest.backend.models.enums.Curriculum.ArticuloCurriculum;
import com.apirest.backend.models.enums.Curriculum.LibroResultadoInvestigacionCurriculum;
import com.apirest.backend.models.enums.Curriculum.TiposProduccionBibliograficaCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Publicacion {
    @Id
    private String id;
    private ArticuloCurriculum articulo;
    private String nombreArticulo;
    private LibroResultadoInvestigacionCurriculum libroResultadoInvestigacion;
    private String nombreLibroRevista;
    private TiposProduccionBibliograficaCurriculum tiposProduccionBibliografica;
    private String nombrePublicacion;
}
