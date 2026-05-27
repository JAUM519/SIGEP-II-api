package com.apirest.backend.jwts;

import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.repositories.IUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_RutaPublica_NoProcesaToken() throws Exception {
        request.setRequestURI("/api/auth/login");
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, usuarioRepository);
    }

    @Test
    void doFilterInternal_SinToken_ContinuaCadena() throws Exception {
        request.setRequestURI("/api/curriculum/test");
        filter.doFilterInternal(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService, usuarioRepository);
    }

    @Test
    void doFilterInternal_ConTokenValido_EstableceAutenticacion() throws Exception {
        request.setRequestURI("/api/curriculum/test");
        request.addHeader("Authorization", "Bearer tokenValido");
        when(jwtService.getUsuarioIdFromToken("tokenValido")).thenReturn("usuarioId");
        UsuarioModelo usuario = UsuarioModelo.builder()
                .id("usuarioId")
                .rol(com.apirest.backend.models.enums.Usuario.RolUsuarios.servidorPublico)
                .build();
        when(usuarioRepository.findById("usuarioId")).thenReturn(Optional.of(usuario));
        when(jwtService.validarToken("tokenValido", usuario)).thenReturn(true);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("usuarioId", ((UsuarioModelo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    @Test
    void doFilterInternal_ConTokenInvalido_NoEstableceAutenticacion() throws Exception {
        request.setRequestURI("/api/curriculum/test");
        request.addHeader("Authorization", "Bearer tokenInvalido");
        when(jwtService.getUsuarioIdFromToken("tokenInvalido")).thenReturn("usuarioId");
        when(usuarioRepository.findById("usuarioId")).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}