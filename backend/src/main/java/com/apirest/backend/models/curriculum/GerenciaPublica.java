package com.apirest.backend.models.curriculum;

import com.apirest.backend.models.curriculum.sections.ParticipacionCorporacionEntidad;
import com.apirest.backend.models.curriculum.sections.ParticipacionProyecto;
import com.apirest.backend.models.curriculum.sections.PremioReconocimiento;
import com.apirest.backend.models.curriculum.sections.Publicacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GerenciaPublica {
    private ArrayList<Publicacion> publicaciones;
    private ArrayList<PremioReconocimiento> premiosReconocimientos;
    private ArrayList<ParticipacionProyecto> participacionesProyectos;
    private ArrayList<ParticipacionCorporacionEntidad> participacionesCorporacionesEntidades;
}
