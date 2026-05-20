package com.apirest.backend.models.curriculum.sections;

import com.apirest.backend.models.enums.Curriculum.MedioCapacitacionCurriculum;
import com.apirest.backend.models.enums.Curriculum.ModalidadCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducacionTrabajo {
    @Id
    private String id;
    private Instant fechaFinalizacion;
    private Integer numeroTotalHoras;
    private String pais;
    private String nombre;
    private String institucion;
    private MedioCapacitacionCurriculum medioCapacitacion;
    private ModalidadCurriculum modalidad;
    private String diplomaActaCertificadoEstudio;


}
