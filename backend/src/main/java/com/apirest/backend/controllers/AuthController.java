package com.apirest.backend.controllers;

import com.apirest.backend.dtos.requests.usuarios.*;
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest usuarioRequest) {
        return ResponseEntity.ok(authService.login(usuarioRequest));
    }

    @PreAuthorize("hasRole('jefeDeTalentoHumano')")
    @PostMapping("/registro")
    public ResponseEntity<Void> crearUsuario(@Valid @RequestBody NuevoUsuarioRequest usuarioRequest){
        authService.crearUsuario(usuarioRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('jefeDeTalentoHumano')")
    @PutMapping("/inhabilitarUsuario")
    public ResponseEntity<Void> inhabilitarUsuario(@Valid @RequestBody InhabilitarRequest usuarioRequest){
        authService.inhabilitarUsuario(usuarioRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/cambiarContraseña")
    public ResponseEntity<Void> cambiarContraseña(@AuthenticationPrincipal UsuarioModelo usuario, @Valid @RequestBody CambiarContraseñaRequest contraseñaNueva){
        authService.cambiarContraseña(usuario.getId(), contraseñaNueva);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/pedirEnlace")
    public ResponseEntity<String> pedirEnlaceEmail(@Valid @RequestBody PedirEnlaceEmailRequest usuarioRequest){
        authService.pedirEnlaceEmail(usuarioRequest);
        return ResponseEntity.ok("Se ha enviado un enlace a su correo electronico. ");
    }

    @PostMapping("/recuperarContraseña")
    public ResponseEntity<String> recuperarContraseña(@RequestParam String token, @Valid @RequestBody CambiarContraseñaRequest usuarioRequest){
        authService.cambiarContraseñaDesdeEmail(token, usuarioRequest);
        return ResponseEntity.ok("Contraseña cambiada papi al toque su rey. ");
    }

}
