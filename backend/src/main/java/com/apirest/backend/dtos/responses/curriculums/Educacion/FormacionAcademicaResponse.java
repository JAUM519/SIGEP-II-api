package com.apirest.backend.dtos.responses.curriculums.Educacion;

import com.apirest.backend.models.enums.Curriculum.AreaConocimientoCurriculum;
import com.apirest.backend.models.enums.Curriculum.EstadoEstudioCurriculum;
import com.apirest.backend.models.enums.Curriculum.NivelAcademicoCurriculum;
import com.apirest.backend.models.enums.Curriculum.NivelFormacionCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormacionAcademicaResponse {
    private String id;
    private NivelAcademicoCurriculum nivelAcademico;
    private NivelFormacionCurriculum nivelFormacion;
    private AreaConocimientoCurriculum areaConocimiento;
    private String pais;
    private String institucion;
    private String programaAcademico;
    private String tituloObtenido;
    private Integer semestresAprobados;
    private EstadoEstudioCurriculum estadoEstudio;
    private Instant fechaTerminacionMaterias;
    private Instant fechaGrado;
    private Boolean estudioConvalidado;
    private Instant fechaConvalidacion;
    private String tarjetaProfesional;
    private Instant estudioExterior;
    private String archivoTarjetaProfesioal;
    private Boolean verificTarjetaProfesional;
    private String archivoEducacionFormal;
    private Boolean verificEducacionFormal;
}
