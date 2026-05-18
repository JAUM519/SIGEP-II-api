package com.apirest.backend.controllers;

import com.apirest.backend.dtos.requests.curriculums.DatosPersonales.*;
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
}
