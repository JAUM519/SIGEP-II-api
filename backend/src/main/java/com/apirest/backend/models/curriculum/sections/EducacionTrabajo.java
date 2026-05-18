package com.apirest.backend.models.curriculum.sections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EducacionTrabajo {
    @id
    private String id;
    private Instant fechaFinalizacion;
    private Integer numeroTotalHoras;
    private String pais;
    private String nombre;
    private String institucion;
    private String medioCapacitacion;
    private String modalidad;
    private String diplomaActaCertificadoEstudio;


}
