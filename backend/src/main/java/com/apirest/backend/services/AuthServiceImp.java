package com.apirest.backend.services;

import com.apirest.backend.dtos.requests.*;
import com.apirest.backend.dtos.responses.LoginResponse;
import com.apirest.backend.exceptions.InvalidCredentialsException;
import com.apirest.backend.exceptions.UserAlreadyExistsException;
import com.apirest.backend.exceptions.UserNotFoundException;
import com.apirest.backend.jwts.JwtService;
import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.models.enums.RolUsuarios;
import com.apirest.backend.repositories.IUsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthServiceImp implements IAuthService{

    private final IUsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImp(IUsuarioRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public LoginResponse login(LoginRequest usuarioRequest) {
        Optional<UsuarioModelo> usuarioExiste = usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(usuarioRequest.getNumeroIdentificacion(), usuarioRequest.getTipoIdentificacion());
        if (!usuarioExiste.isPresent()) {
            throw new UserNotFoundException(usuarioRequest.getNumeroIdentificacion());
        }
        UsuarioModelo usuarioFinal = usuarioExiste.get();
        if(!passwordEncoder.matches(usuarioRequest.getContraseña(), usuarioFinal.getContraseña())){
            throw new InvalidCredentialsException("Credenciales invalidas. ");
        }
        if(!usuarioFinal.isEstadoActivo()){
            throw new InvalidCredentialsException("Estado inactivo. ");
        }
        if (usuarioFinal.getFechaFin() != null && !usuarioFinal.getFechaFin().isAfter(Instant.now())){
            usuarioFinal.setEstadoActivo(false);
            usuarioRepository.save(usuarioFinal);
            throw new InvalidCredentialsException("Estado inactivo. ");
        }
        String tokenUsuario = jwtService.generarToken(usuarioFinal.getId(), usuarioFinal.getRol(), usuarioFinal.getNumeroIdentificacion());
        LoginResponse usuarioResponse = LoginResponse.builder()
                .tipoIdentificacion(usuarioFinal.getTipoIdentificacion())
                .numeroIdentificacion(usuarioFinal.getNumeroIdentificacion())
                .token(tokenUsuario)
                .build();
        return usuarioResponse;
    }

    @Override
    @Transactional
    public void recuperarContraseña(RecuperarConRequest usuarioRequest) {

    }

    @Override
    public void cambiarContraseña(String idUsuario, CambiarContraseña contraseñaNueva) {
        Optional<UsuarioModelo> usuarioExiste = usuarioRepository.findById(idUsuario);
        if (!usuarioExiste.isPresent()){
            throw new UserNotFoundException(idUsuario);
        }
        UsuarioModelo usuarioFinal = usuarioExiste.get();
        usuarioFinal.setContraseña(passwordEncoder.encode(contraseñaNueva.getContraseña()));
        usuarioRepository.save(usuarioFinal);
    }

    @Override
    public void crearUsuario(NuevoUsuarioRequest usuarioRequest) {
        Optional<UsuarioModelo> usuarioExiste = usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(usuarioRequest.getNumeroIdentificacion(), usuarioRequest.getTipoIdentificacion());
        if (usuarioExiste.isPresent()) {
            throw new UserAlreadyExistsException("El tipo y número de identificación ya están en uso. ");
        }
        Optional<UsuarioModelo> usuarioExisteEmail = usuarioRepository.findByEmail(usuarioRequest.getEmail());
        if (usuarioExisteEmail.isPresent()) {
            throw new UserAlreadyExistsException("Este email ya está en uso. ");
        }
        UsuarioModelo usuarioFinal = UsuarioModelo.builder()
                .tipoIdentificacion(usuarioRequest.getTipoIdentificacion())
                .numeroIdentificacion(usuarioRequest.getNumeroIdentificacion())
                .email(usuarioRequest.getEmail())
                .contraseña(passwordEncoder.encode(usuarioRequest.getContraseña()))
                .rol(RolUsuarios.servidorPublico)
                .estadoActivo(true)
                .build();

        usuarioRepository.save(usuarioFinal);

    }

    @Override
    public void inhabilitarUsuario(InhabilitarRequest usuarioRequest) {
        Optional<UsuarioModelo> usuarioExiste = usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(usuarioRequest.getNumeroIdentificacion(), usuarioRequest.getTipoIdentificacion());
        if (!usuarioExiste.isPresent()) {
            throw new UserNotFoundException(usuarioRequest.getNumeroIdentificacion());
        }
        UsuarioModelo usuarioFinal = usuarioExiste.get();

        usuarioFinal.setFechaFin(usuarioRequest.getFechaFin());
        usuarioRepository.save(usuarioFinal);

    }
}
