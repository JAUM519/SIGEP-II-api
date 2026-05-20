package com.apirest.backend.jwts;

import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.models.enums.Usuario.RolUsuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "llaveScreta", "tp81f40tGgJwpGzePE2A33g/WJhmb+hprud2Oxzi+yY=");
        ReflectionTestUtils.setField(jwtService, "expiracionMinutos", 3600000L);
    }

    @Test
    void generarToken_And_ValidarToken() {
        String usuarioId = "123";
        RolUsuarios rol = RolUsuarios.servidorPublico;
        String numeroIdentificacion = "123456";
        String token = jwtService.generarToken(usuarioId, rol, numeroIdentificacion);
        assertNotNull(token);

        UsuarioModelo usuario = UsuarioModelo.builder()
                .id(usuarioId)
                .rol(rol)
                .build();
        boolean valido = jwtService.validarToken(token, usuario);
        assertTrue(valido);
    }

    @Test
    void getUsuarioIdFromToken_ReturnsCorrectId() {
        String usuarioId = "123";
        String token = jwtService.generarToken(usuarioId, RolUsuarios.servidorPublico, "123456");
        String extractedId = jwtService.getUsuarioIdFromToken(token);
        assertEquals(usuarioId, extractedId);
    }

    @Test
    void getClaimByName_ReturnsClaim() {
        String token = jwtService.generarToken("123", RolUsuarios.servidorPublico, "123456");
        Object rol = jwtService.getClaimByName(token, "rol");
        assertEquals(RolUsuarios.servidorPublico.name(), rol.toString());
    }

    @Test
    void isExpiradoToken_ValidToken_ReturnsFalse() {
        String token = jwtService.generarToken("123", RolUsuarios.servidorPublico, "123456");
        assertFalse(jwtService.isExpiradoToken(token));
    }

    @Test
    void isExpiradoToken_InvalidToken_ReturnsTrue() {
        boolean expirado = jwtService.isExpiradoToken("invalid.token.string");
        assertTrue(expirado);
    }
}