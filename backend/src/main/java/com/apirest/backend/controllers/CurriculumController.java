package com.apirest.backend.controllers;

import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarEducacionTrabajoRequest;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarFormacionAcademicaRequest;
import com.apirest.backend.dtos.requests.curriculums.Educacion.RegistrarIdiomaRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralDocenteRequest;
import com.apirest.backend.dtos.requests.curriculums.ExperienciaLaboral.RegistrarExperienciaLaboralRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionCorporacionEntidadRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarParticipacionProyectoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPremioReconocimientoRequest;
import com.apirest.backend.dtos.requests.curriculums.GerenciaPublica.RegistrarPublicacionRequest;
import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.services.ICurriculumService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

    private final ICurriculumService curriculumService;

    public CurriculumController(ICurriculumService curriculumService) {
        this.curriculumService = curriculumService;
    }

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

}
