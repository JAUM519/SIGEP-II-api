package com.apirest.backend.controllers;

import com.apirest.backend.dtos.requests.usuarios.*;
import com.apirest.backend.dtos.responses.usuarios.LoginResponse;
import com.apirest.backend.jwts.JwtService;
import com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios;
import com.apirest.backend.repositories.IUsuarioRepository;
import com.apirest.backend.services.IAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IAuthService authService;

    @MockBean
    private JwtService jwtService; // necesario para cargar el contexto

    @MockBean
    private IUsuarioRepository usuarioRepository; // necesario para cargar el contexto

    // Helper para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    void login_ReturnsOk() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        request.setNumeroIdentificacion("123");
        request.setContraseña("pass");
        LoginResponse response = LoginResponse.builder()
                .tipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania)
                .numeroIdentificacion("123")
                .token("fake-jwt-token")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt-token"));
    }

    @Test
    @WithMockUser(roles = {"jefeDeTalentoHumano"})
    void crearUsuario_ReturnsCreated() throws Exception {
        NuevoUsuarioRequest request = new NuevoUsuarioRequest();
        request.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        request.setNumeroIdentificacion("987654321");
        request.setEmail("nuevo@test.com");
        request.setContraseña("password123");

        doNothing().when(authService).crearUsuario(any(NuevoUsuarioRequest.class));

        mockMvc.perform(post("/api/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = {"jefeDeTalentoHumano"})
    void inhabilitarUsuario_ReturnsOk() throws Exception {
        InhabilitarRequest request = new InhabilitarRequest();
        request.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        request.setNumeroIdentificacion("123456789");
        request.setFechaFin(Instant.now().plusSeconds(3600));

        doNothing().when(authService).inhabilitarUsuario(any(InhabilitarRequest.class));

        mockMvc.perform(put("/api/auth/inhabilitarUsuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk());
    }



    @Test
    void pedirEnlaceEmail_ReturnsOk() throws Exception {
        PedirEnlaceEmailRequest request = new PedirEnlaceEmailRequest();
        request.setTipoIdentificacion(TipoIdentificacionUsuarios.CedulaDeCiudadania);
        request.setNumeroIdentificacion("123456789");

        doNothing().when(authService).pedirEnlaceEmail(any(PedirEnlaceEmailRequest.class));

        mockMvc.perform(post("/api/auth/pedirEnlace")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Se ha enviado un enlace a su correo electronico. "));
    }


}