package com.apirest.backend.services;

import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
import com.apirest.backend.dtos.requests.curriculums.Educacion.*;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.ActualizarExperienciaLaboralDocenteRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.ActualizarExperienciaLaboralRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralDocenteRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionCorporacionEntidadRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionProyectoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPremioReconocimientoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPublicacionRequest;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosBasicosResponse;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosContactoResponse;
import com.apirest.backend.dtos.responses.curriculums.DatosPersonales.DatosDemograficosResponse;
import com.apirest.backend.dtos.responses.curriculums.Educacion.EducacionTrabajoResponse;
import com.apirest.backend.dtos.responses.curriculums.Educacion.FormacionAcademicaResponse;
import com.apirest.backend.dtos.responses.curriculums.Educacion.IdiomaResponse;
import com.apirest.backend.dtos.responses.curriculums.ExperienciaLaboral.ExperienciaLaboralDocenteResponse;
import com.apirest.backend.dtos.responses.curriculums.ExperienciaLaboral.ExperienciaLaboralResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.ParticipacionCorporacionEntidadResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.ParticipacionProyectoResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.PremioReconocimientoResponse;
import com.apirest.backend.dtos.responses.curriculums.GerenciaPublica.PublicacionResponse;
import com.apirest.backend.models.curriculum.ExperienciaLaboral;

import java.util.List;

public interface ICurriculumService {
    //DatosPersonales
    public void registrarDatosPersonalesBasicos(String usuarioId, RegistrarDatosBasicosRequest curriculumRequest);
    public void actualizarDatosPersonalesBasicos(String usuarioId , ActualizarDatosBasicosRequest curriculumRequest);
    public void registrarDatosDemograficos(String usuarioId , RegistrarDatosDemograficosRequest curriculumRequest);
    public void actualizarDatosDemograficos(String usuarioId , ActualizarDatosDemograficosRequest curriculumRequest);
    public void registrarDatosContacto(String usuarioId, RegistrarDatosContactoRequest curriculumRequest);
    public void actualizarDatosContacto(String usuarioId, ActualizarDatosContactoRequest curriculumRequest);
    public DatosBasicosResponse obtenerDatosBasicos(String usuarioId);
    public DatosDemograficosResponse obtenerDatosDemograficos(String usuarioId);
    public DatosContactoResponse obtenerDatosContacto(String usuarioId);

    //Educacion
    public void registrarFormacionAcademica(String usuarioId, RegistrarFormacionAcademicaRequest curriculumRequest);
    public void actualizarFormacionAcademica(String usuarioId, ActualizarFormacionAcademicaRequest curriculumRequest);
    public void registrarEducacionTrabajo(String usuarioId, RegistrarEducacionTrabajoRequest curriculumRequest);
    public void actualizarEducacionTrabajo(String usuarioId, ActualizarEducacionTrabajoRequest curriculumRequest);
    public void registrarIdioma(String usuarioId, RegistrarIdiomaRequest curriculumRequest);
    public void actualizarIdioma(String usuarioId, ActualizarIdiomaRequest curriculumRequest);
    List<FormacionAcademicaResponse> obtenerTodasFormacionesAcademicas(String usuarioId);
    List<EducacionTrabajoResponse> obtenerTodaEducacionTrabajo(String usuarioId);
    List<IdiomaResponse> obtenerTodosIdiomas(String usuarioId);
    public FormacionAcademicaResponse obtenerFormacionAcademica(String usuarioId, String formacionId);
    public EducacionTrabajoResponse obtenerEducacionTrabajo(String usuarioId, String educacionId);
    public IdiomaResponse obtenerIdioma(String usuarioId, String idiomaId);

    //Experiencias
    public void registrarExperienciaLaboral(String usuarioId, RegistrarExperienciaLaboralRequest curriculumRequest);
    public void actualizarExperienciaLaboral(String usuarioId, ActualizarExperienciaLaboralRequest curriculumRequest);
    public void registrarExperienciaLaboralDocente(String usuarioId, RegistrarExperienciaLaboralDocenteRequest curriculumRequest);
    public void actualizarExperienciaLaboralDocente(String usuarioId, ActualizarExperienciaLaboralDocenteRequest curriculumRequest);
    List<ExperienciaLaboralResponse> obtenerTodasExperienciaLaboral(String usuarioId);
    List<ExperienciaLaboralDocenteResponse> obtenerTodasExperienciaLaboralDocente(String usuarioId);
    public ExperienciaLaboralResponse obtenerExperienciaLaboral(String usuarioId, String experienciaLaboralId);
    public ExperienciaLaboralDocenteResponse obtenerExperienciaLaboralDocente(String usuarioId, String experienciaLaboralId);

    //GerenciaPublica
    public void registrarPublicacion(String usuarioId, RegistrarPublicacionRequest curriculumRequest);
    public void registrarPremioReconocimiento(String usuarioId, RegistrarPremioReconocimientoRequest curriculumRequest);
    public void registrarParticipacionProyecto(String usuarioId, RegistrarParticipacionProyectoRequest curriculumRequest);
    public void registrarParticipacionCorporacionEntidad(String usuarioId, RegistrarParticipacionCorporacionEntidadRequest curriculumRequest);
    public List<PublicacionResponse> obtenerTodasPublicaciones(String usuarioId);
    public List<PremioReconocimientoResponse> obtenerTodosPremiosReconocimientos(String usuarioId);
    public List<ParticipacionProyectoResponse> obtenerTodasParticipacionesProyectos(String usuarioId);
    public List<ParticipacionCorporacionEntidadResponse> obtenerTodasParticipacionesCorporacionesEntidades(String usuarioId);
    public PublicacionResponse obtenerPublicacionPorId(String usuarioId, String publicacionId);
    public PremioReconocimientoResponse obtenerPremioReconocimientoPorId(String usuarioId, String premioId);
    public ParticipacionProyectoResponse obtenerParticipacionProyectoPorId(String usuarioId, String participacionId);
    public ParticipacionCorporacionEntidadResponse obtenerParticipacionCorporacionEntidadPorId(String usuarioId, String corporacionId);




}
