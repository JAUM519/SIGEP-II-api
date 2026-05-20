package com.apirest.backend.models.curriculum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document("curriculums")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumModelo {
    @Id
    private String id;
    private String usuarioId;
    private DatosPersonales datosPersonales;
    private Educacion educacion;
    private ArrayList<ExperienciaLaboral> experienciasLaborales;
    private ArrayList<ExperienciaLaboralDocente> experienciasLaboralesDocente;
    private ArrayList<DocumentoAdicional> documentosAdicionales;
    private GerenciaPublica gerenciaPublica;

}
