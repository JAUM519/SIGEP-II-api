package com.apirest.backend.services;

import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarEducacionTrabajoRequest;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarFormacionAcademicaRequest;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarIdiomaRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralDocenteRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralRequest;

public interface ICurriculumService {
    //DatosPersonales
    public void registrarDatosPersonalesBasicos(String usuarioId, RegistrarDatosBasicosRequest curriculumRequest);
    public void actualizarDatosPersonalesBasicos(String usuarioId , ActualizarDatosBasicosRequest curriculumRequest);
    public void registrarDatosDemograficos(String usuarioId , RegistrarDatosDemograficosRequest curriculumRequest);
    public void actualizarDatosDemograficos(String usuarioId , ActualizarDatosDemograficosRequest curriculumRequest);
    public void registrarDatosContacto(String usuarioId, RegistrarDatosContactoRequest curriculumRequest);
    public void actualizarDatosContacto(String usuarioId, ActualizarDatosContactoRequest curriculumRequest);
    //Educacion
    public void registrarFormacionAcademica(String usuarioId, RegistrarFormacionAcademicaRequest curriculumRequest);
    public void registrarEducacionTrabajo(String usuarioId, RegistrarEducacionTrabajoRequest curriculumRequest);
    public void registrarIdioma(String usuarioId, RegistrarIdiomaRequest curriculumRequest);
    //Experiencias
    public void registrarExperienciaLaboral(String usuarioId, RegistrarExperienciaLaboralRequest curriculumRequest);
    public void registrarExperienciaLaboralDocente(String usuarioId, RegistrarExperienciaLaboralDocenteRequest curriculumRequest);




}
