package com.apirest.backend.services;

import com.apirest.backend.dtos.requests.*;
import com.apirest.backend.dtos.responses.LoginResponse;

public interface IAuthService {
    public LoginResponse login(LoginRequest usuarioRequest);
    public void pedirEnlaceEmail(PedirEnlaceEmailRequest usuarioRequest);
    public void cambiarContraseñaDesdeEmail(String token, CambiarContraseñaRequest usuarioRequest);
    public void cambiarContraseña(String idUsuario, CambiarContraseñaRequest contraseña);
    public void crearUsuario(NuevoUsuarioRequest usuarioRequest);
    public void inhabilitarUsuario(InhabilitarRequest usuarioRequest);
}
