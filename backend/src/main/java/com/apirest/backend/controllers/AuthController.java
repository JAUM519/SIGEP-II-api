package com.apirest.backend.controllers;

import com.apirest.backend.dtos.requests.LoginRequest;
import com.apirest.backend.dtos.requests.NuevoUsuarioRequest;
import com.apirest.backend.dtos.responses.LoginResponse;
import com.apirest.backend.services.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
