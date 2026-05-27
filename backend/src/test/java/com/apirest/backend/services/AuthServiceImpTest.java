package com.apirest.backend.services;

import com.apirest.backend.dtos.requests.usuarios.*;
import com.apirest.backend.dtos.responses.usuarios.LoginResponse;
import com.apirest.backend.exceptions.InvalidCredentialsException;
import com.apirest.backend.exceptions.UserAlreadyExistsException;
import com.apirest.backend.exceptions.UserNotFoundException;
import com.apirest.backend.jwts.JwtService;
import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.models.enums.Usuario.RolUsuarios;
import com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios;
import com.apirest.backend.repositories.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImpTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthServiceImp authService;

    private UsuarioModelo usuario;
    private String usuarioId;
    private LoginRequest loginRequest;
    private PedirEnlaceEmailRequest pedirEnlaceRequest;
    private NuevoUsuarioRequest nuevoUsuarioRequest;
    private InhabilitarRequest inhabilitarRequest;

    @BeforeEach
    void setUp() {
        usuarioId = "67f0a1b2c3d4e5f6a7b8c9d0";
        usuario = UsuarioModelo.builder()
                .id(usuarioId)
                .tipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania)
                .numeroIdentificacion("123456789")
                .contraseña("encodedPass")
                .email("test@test.com")
                .rol(RolUsuarios.servidorPublico)
                .estadoActivo(true)
                .fechaFin(null)
                .build();

        loginRequest = new LoginRequest();
        loginRequest.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        loginRequest.setNumeroIdentificacion("123456789");
        loginRequest.setContraseña("password123");

        pedirEnlaceRequest = new PedirEnlaceEmailRequest();
        pedirEnlaceRequest.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        pedirEnlaceRequest.setNumeroIdentificacion("123456789");

        nuevoUsuarioRequest = new NuevoUsuarioRequest();
        nuevoUsuarioRequest.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        nuevoUsuarioRequest.setNumeroIdentificacion("987654321");
        nuevoUsuarioRequest.setEmail("nuevo@test.com");
        nuevoUsuarioRequest.setContraseña("newPass123");

        inhabilitarRequest = new InhabilitarRequest();
        inhabilitarRequest.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        inhabilitarRequest.setNumeroIdentificacion("123456789");
        inhabilitarRequest.setFechaFin(Instant.now().plusSeconds(3600));
    }

    // ==================== login ====================
    @Test
    void login_Success() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generarToken(anyString(), any(), anyString())).thenReturn("fakeToken");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("fakeToken", response.getToken());
        assertEquals(usuario.getTipoIdentificacion(), response.getTipoIdentificacion());
        assertEquals(usuario.getNumeroIdentificacion(), response.getNumeroIdentificacion());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_InactiveUser_ThrowsException() {
        usuario.setEstadoActivo(false);
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_ExpiredFechaFin_UpdatesAndThrowsException() {
        // Fecha pasada
        usuario.setFechaFin(Instant.now().minusSeconds(1));
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true); //
        // No es necesario mockear save, solo verificar que se llama
        when(usuarioRepository.save(any(UsuarioModelo.class))).thenReturn(usuario);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));

        verify(usuarioRepository).save(usuario);
        assertFalse(usuario.getEstadoActivo());
    }

    // ==================== pedirEnlaceEmail ====================
    @Test
    void pedirEnlaceEmail_Success() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));
        when(jwtService.generarTokenRecuperacion(anyString(), any(), anyString())).thenReturn("recToken");

        authService.pedirEnlaceEmail(pedirEnlaceRequest);

        verify(jwtService).generarTokenRecuperacion(usuarioId, usuario.getRol(), usuario.getNumeroIdentificacion());
        verify(emailService).enviarEnlaceRecuperacion(eq(usuario.getEmail()), anyString());
    }

    @Test
    void pedirEnlaceEmail_UserNotFound_ThrowsException() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.pedirEnlaceEmail(pedirEnlaceRequest));
    }

    @Test
    void pedirEnlaceEmail_InactiveUser_ThrowsException() {
        usuario.setEstadoActivo(false);
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));

        assertThrows(InvalidCredentialsException.class, () -> authService.pedirEnlaceEmail(pedirEnlaceRequest));
    }

    // ==================== cambiarContraseñaDesdeEmail ====================
    @Test
    void cambiarContraseñaDesdeEmail_Success() {
        String token = "validToken";
        CambiarContraseñaRequest request = new CambiarContraseñaRequest();
        request.setContraseña("newPass123");

        when(jwtService.isExpiradoToken(token)).thenReturn(false);
        when(jwtService.getClaimByName(token, "proposito")).thenReturn("recuperar_contraseña");
        when(jwtService.getUsuarioIdFromToken(token)).thenReturn(usuarioId);
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newPass123")).thenReturn("encodedNew");

        authService.cambiarContraseñaDesdeEmail(token, request);

        verify(usuarioRepository).save(usuario);
        assertEquals("encodedNew", usuario.getContraseña());
    }

    @Test
    void cambiarContraseñaDesdeEmail_ExpiredToken_ThrowsException() {
        String token = "expiredToken";
        when(jwtService.isExpiradoToken(token)).thenReturn(true);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.cambiarContraseñaDesdeEmail(token, new CambiarContraseñaRequest()));
    }

    @Test
    void cambiarContraseñaDesdeEmail_WrongPurpose_ThrowsException() {
        String token = "token";
        when(jwtService.isExpiradoToken(token)).thenReturn(false);
        when(jwtService.getClaimByName(token, "proposito")).thenReturn("otro");

        assertThrows(InvalidCredentialsException.class,
                () -> authService.cambiarContraseñaDesdeEmail(token, new CambiarContraseñaRequest()));
    }

    @Test
    void cambiarContraseñaDesdeEmail_UserNotFound_ThrowsException() {
        String token = "token";
        when(jwtService.isExpiradoToken(token)).thenReturn(false);
        when(jwtService.getClaimByName(token, "proposito")).thenReturn("recuperar_contraseña");
        when(jwtService.getUsuarioIdFromToken(token)).thenReturn("userIdNotFound");
        when(usuarioRepository.findById("userIdNotFound")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authService.cambiarContraseñaDesdeEmail(token, new CambiarContraseñaRequest()));
    }

    // ==================== cambiarContraseña (autenticado) ====================
    @Test
    void cambiarContraseña_Success() {
        CambiarContraseñaRequest request = new CambiarContraseñaRequest();
        request.setContraseña("newPass");
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNew");

        authService.cambiarContraseña(usuarioId, request);

        verify(usuarioRepository).save(usuario);
        assertEquals("encodedNew", usuario.getContraseña());
    }

    @Test
    void cambiarContraseña_UserNotFound_ThrowsException() {
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> authService.cambiarContraseña(usuarioId, new CambiarContraseñaRequest()));
    }

    // ==================== crearUsuario ====================
    @Test
    void crearUsuario_Success() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(usuarioRepository.save(any(UsuarioModelo.class))).thenAnswer(i -> i.getArgument(0));

        authService.crearUsuario(nuevoUsuarioRequest);

        verify(usuarioRepository).save(any(UsuarioModelo.class));
    }

    @Test
    void crearUsuario_ExistingIdentificacion_ThrowsException() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));

        assertThrows(UserAlreadyExistsException.class, () -> authService.crearUsuario(nuevoUsuarioRequest));
    }

    @Test
    void crearUsuario_ExistingEmail_ThrowsException() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        assertThrows(UserAlreadyExistsException.class, () -> authService.crearUsuario(nuevoUsuarioRequest));
    }

    // ==================== inhabilitarUsuario ====================
    @Test
    void inhabilitarUsuario_Success() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.of(usuario));

        authService.inhabilitarUsuario(inhabilitarRequest);

        verify(usuarioRepository).save(usuario);
        assertNotNull(usuario.getFechaFin());
    }

    @Test
    void inhabilitarUsuario_UserNotFound_ThrowsException() {
        when(usuarioRepository.findByNumeroIdentificacionAndTipoIdentificacion(anyString(), any()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.inhabilitarUsuario(inhabilitarRequest));
    }
}