package com.apirest.backend.controllers;

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
import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.services.ICurriculumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

    private final ICurriculumService curriculumService;

    public CurriculumController(ICurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

    //DatosPersonales
    @PostMapping("/datosPersonales/datosBasicos")
    public ResponseEntity<Void> registrarDatosPersonalesBasicos(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarDatosBasicosRequest curriculumRequest){
        curriculumService.registrarDatosPersonalesBasicos(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/datosPersonales/datosBasicos")
    public ResponseEntity<Void> actualizarDatosPersonalesBasicos(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarDatosBasicosRequest curriculumRequest) {
        curriculumService.actualizarDatosPersonalesBasicos(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/datosPersonales/datosDemograficos")
    public ResponseEntity<Void> registrarDatosDemograficos(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarDatosDemograficosRequest curriculumRequest) {
        curriculumService.registrarDatosDemograficos(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/datosPersonales/datosDemograficos")
    public ResponseEntity<Void> actualizarDatosDemograficos(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarDatosDemograficosRequest curriculumRequest) {
        curriculumService.actualizarDatosDemograficos(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/datosPersonales/datosContacto")
    public ResponseEntity<Void> registrarDatosContacto(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarDatosContactoRequest curriculumRequest) {
        curriculumService.registrarDatosContacto(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/datosPersonales/datosContacto")
    public ResponseEntity<Void> actualizarDatosContacto(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarDatosContactoRequest curriculumRequest) {
        curriculumService.actualizarDatosContacto(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/datosPersonales/datosBasicos")
    public ResponseEntity<DatosBasicosResponse> obtenerDatosBasicos(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerDatosBasicos(usuario.getId()));
    }

    @GetMapping("/datosPersonales/datosDemograficos")
    public ResponseEntity<DatosDemograficosResponse> obtenerDatosDemograficos(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerDatosDemograficos(usuario.getId()));
    }

    @GetMapping("/datosPersonales/datosContacto")
    public ResponseEntity<DatosContactoResponse> obtenerDatosContacto(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerDatosContacto(usuario.getId()));
    }

    //Educacion
    @PostMapping("/educacion/formacionAcademica")
    public ResponseEntity<Void> registrarFormacionAcademica(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarFormacionAcademicaRequest curriculumRequest) {
        curriculumService.registrarFormacionAcademica(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/educacion/trabajo")
    public ResponseEntity<Void> registrarEducacionTrabajo(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarEducacionTrabajoRequest curriculumRequest) {
        curriculumService.registrarEducacionTrabajo(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/educacion/idioma")
    public ResponseEntity<Void> registrarIdioma(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarIdiomaRequest curriculumRequest) {
        curriculumService.registrarIdioma(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/educacion/formacionAcademica")
    public ResponseEntity<Void> actualizarFormacionAcademica(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarFormacionAcademicaRequest curriculumRequest) {
        curriculumService.actualizarFormacionAcademica(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/educacion/trabajo")
    public ResponseEntity<Void> actualizarEducacionTrabajo(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarEducacionTrabajoRequest curriculumRequest) {
        curriculumService.actualizarEducacionTrabajo(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/educacion/idioma")
    public ResponseEntity<Void> actualizarIdioma(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarIdiomaRequest curriculumRequest) {
        curriculumService.actualizarIdioma(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/educacion/formacionAcademica")
    public ResponseEntity<List<FormacionAcademicaResponse>> obtenerTodasFormacionesAcademicas(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodasFormacionesAcademicas(usuario.getId()));
    }

    @GetMapping("/educacion/trabajo")
    public ResponseEntity<List<EducacionTrabajoResponse>> obtenerTodaEducacionTrabajo(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodaEducacionTrabajo(usuario.getId()));
    }

    @GetMapping("/educacion/idioma")
    public ResponseEntity<List<IdiomaResponse>> obtenerTodosIdiomas(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodosIdiomas(usuario.getId()));
    }

    @GetMapping("/educacion/formacionAcademica/{formacionId}")
    public ResponseEntity<FormacionAcademicaResponse> obtenerFormacionAcademica(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String formacionId) {
        return ResponseEntity.ok(curriculumService.obtenerFormacionAcademica(usuario.getId(), formacionId));
    }

    @GetMapping("/educacion/trabajo/{educacionId}")
    public ResponseEntity<EducacionTrabajoResponse> obtenerEducacionTrabajo(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String educacionId) {
        return ResponseEntity.ok(curriculumService.obtenerEducacionTrabajo(usuario.getId(), educacionId));
    }

    @GetMapping("/educacion/idioma/{idiomaId}")
    public ResponseEntity<IdiomaResponse> obtenerIdioma(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String idiomaId) {
        return ResponseEntity.ok(curriculumService.obtenerIdioma(usuario.getId(), idiomaId));
    }

    //Experiencia
    @PostMapping("/experienciaLaboral")
    public ResponseEntity<Void> registrarExperienciaLaboral(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarExperienciaLaboralRequest curriculumRequest) {
        curriculumService.registrarExperienciaLaboral(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/experienciaLaboral/docente")
    public ResponseEntity<Void> registrarExperienciaLaboralDocente(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarExperienciaLaboralDocenteRequest curriculumRequest) {
        curriculumService.registrarExperienciaLaboralDocente(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/experienciaLaboral")
    public ResponseEntity<Void> actualizarExperienciaLaboral(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarExperienciaLaboralRequest curriculumRequest) {
        curriculumService.actualizarExperienciaLaboral(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/experienciaLaboral/docente")
    public ResponseEntity<Void> actualizarExperienciaLaboralDocente(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody ActualizarExperienciaLaboralDocenteRequest curriculumRequest) {
        curriculumService.actualizarExperienciaLaboralDocente(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/experienciaLaboral")
    public ResponseEntity<List<ExperienciaLaboralResponse>> obtenerTodasExperienciaLaboral(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodasExperienciaLaboral(usuario.getId()));
    }

    @GetMapping("/experienciaLaboral/docente")
    public ResponseEntity<List<ExperienciaLaboralDocenteResponse>> obtenerTodasExperienciaLaboralDocente(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodasExperienciaLaboralDocente(usuario.getId()));
    }

    @GetMapping("/experienciaLaboral/{experienciaLaboralId}")
    public ResponseEntity<ExperienciaLaboralResponse> obtenerExperienciaLaboral(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String experienciaLaboralId) {
        return ResponseEntity.ok(curriculumService.obtenerExperienciaLaboral(usuario.getId(), experienciaLaboralId));
    }

    @GetMapping("/experienciaLaboral/docente/{experienciaLaboralId}")
    public ResponseEntity<ExperienciaLaboralDocenteResponse> obtenerExperienciaLaboralDocente(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String experienciaLaboralId) {
        return ResponseEntity.ok(curriculumService.obtenerExperienciaLaboralDocente(usuario.getId(), experienciaLaboralId));
    }

    //GerenciaPublica
    @PostMapping("/gerenciaPublica/publicacion")
    public ResponseEntity<Void> registrarPublicacion(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarPublicacionRequest curriculumRequest) {
        curriculumService.registrarPublicacion(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/gerenciaPublica/premioReconocimiento")
    public ResponseEntity<Void> registrarPremioReconocimiento(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarPremioReconocimientoRequest curriculumRequest) {
        curriculumService.registrarPremioReconocimiento(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/gerenciaPublica/participacionProyecto")
    public ResponseEntity<Void> registrarParticipacionProyecto(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarParticipacionProyectoRequest curriculumRequest) {
        curriculumService.registrarParticipacionProyecto(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/gerenciaPublica/participacionCorporacionEntidad")
    public ResponseEntity<Void> registrarParticipacionCorporacionEntidad(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody RegistrarParticipacionCorporacionEntidadRequest curriculumRequest) {
        curriculumService.registrarParticipacionCorporacionEntidad(usuario.getId(), curriculumRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/gerenciaPublica/publicacion")
    public ResponseEntity<List<PublicacionResponse>> obtenerTodasPublicaciones(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodasPublicaciones(usuario.getId()));
    }

    @GetMapping("/gerenciaPublica/premioReconocimiento")
    public ResponseEntity<List<PremioReconocimientoResponse>> obtenerTodosPremiosReconocimientos(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodosPremiosReconocimientos(usuario.getId()));
    }

    @GetMapping("/gerenciaPublica/participacionProyecto")
    public ResponseEntity<List<ParticipacionProyectoResponse>> obtenerTodasParticipacionesProyectos(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodasParticipacionesProyectos(usuario.getId()));
    }

    @GetMapping("/gerenciaPublica/participacionCorporacionEntidad")
    public ResponseEntity<List<ParticipacionCorporacionEntidadResponse>> obtenerTodasParticipacionesCorporacionesEntidades(@AuthenticationPrincipal UsuarioModelo usuario) {
        return ResponseEntity.ok(curriculumService.obtenerTodasParticipacionesCorporacionesEntidades(usuario.getId()));
    }

    @GetMapping("/gerenciaPublica/publicacion/{publicacionId}")
    public ResponseEntity<PublicacionResponse> obtenerPublicacionPorId(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String publicacionId) {
        return ResponseEntity.ok(curriculumService.obtenerPublicacionPorId(usuario.getId(), publicacionId));
    }

    @GetMapping("/gerenciaPublica/premioReconocimiento/{premioId}")
    public ResponseEntity<PremioReconocimientoResponse> obtenerPremioReconocimientoPorId(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String premioId) {
        return ResponseEntity.ok(curriculumService.obtenerPremioReconocimientoPorId(usuario.getId(), premioId));
    }

    @GetMapping("/gerenciaPublica/participacionProyecto/{participacionId}")
    public ResponseEntity<ParticipacionProyectoResponse> obtenerParticipacionProyectoPorId(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String participacionId) {
        return ResponseEntity.ok(curriculumService.obtenerParticipacionProyectoPorId(usuario.getId(), participacionId));
    }

    @GetMapping("/gerenciaPublica/participacionCorporacionEntidad/{corporacionId}")
    public ResponseEntity<ParticipacionCorporacionEntidadResponse> obtenerParticipacionCorporacionEntidadPorId(@AuthenticationPrincipal UsuarioModelo usuario, @PathVariable String corporacionId) {
        return ResponseEntity.ok(curriculumService.obtenerParticipacionCorporacionEntidadPorId(usuario.getId(), corporacionId));
    }

}
