package com.apirest.backend.models.curriculum;

import com.apirest.backend.models.curriculum.sections.EducacionTrabajo;
import com.apirest.backend.models.curriculum.sections.FormacionAcademica;
import com.apirest.backend.models.curriculum.sections.Idioma;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Educacion {
    ArrayList<FormacionAcademica> formacionesAcademicas;
    ArrayList<EducacionTrabajo> educacionTrabajos;
    ArrayList<Idioma> idiomas;
}
