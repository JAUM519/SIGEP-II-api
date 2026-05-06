package com.apirest.backend.controllers;

import com.apirest.backend.dtos.requests.CambiarContraseña;
import com.apirest.backend.dtos.requests.InhabilitarRequest;
import com.apirest.backend.dtos.requests.LoginRequest;
import com.apirest.backend.dtos.requests.NuevoUsuarioRequest;
import com.apirest.backend.dtos.responses.LoginResponse;
import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.services.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {


    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest usuarioRequest) {
        return ResponseEntity.ok(authService.login(usuarioRequest));
    }

    @PreAuthorize("hasRole('jefeDeTalentoHumano')")
    @PostMapping("/registro")
    ResponseEntity<Void> crearUsuario(@Valid @RequestBody NuevoUsuarioRequest usuarioRequest){
        authService.crearUsuario(usuarioRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('jefeDeTalentoHumano')")
    @PutMapping("/inhabilitarUsuario")
    ResponseEntity<Void> inhabilitarUsuario(@Valid @RequestBody InhabilitarRequest usuarioRequest){
        authService.inhabilitarUsuario(usuarioRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/cambiarContraseña")
    ResponseEntity<Void> cambiarContraseña(@AuthenticationPrincipal UsuarioModelo usuario, @RequestBody CambiarContraseña contraseñaNueva){
        authService.cambiarContraseña(usuario.getId(), contraseñaNueva);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
