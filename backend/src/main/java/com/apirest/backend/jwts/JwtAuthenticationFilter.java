package com.apirest.backend.jwts;

import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.repositories.IUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/pedirEnlace",
            "/api/auth/recuperarContraseña"
    );

    private final JwtService jwtService;
    private final IUsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtService jwtService, IUsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (PUBLIC_PATHS.stream().anyMatch(requestURI::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = getTokenFromReques(request);
        final String usuarioId;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        usuarioId = jwtService.getUsuarioIdFromToken(token);

        if (usuarioId != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional<UsuarioModelo> usuario = usuarioRepository.findById(usuarioId);

            if (usuario.isPresent()) {
                UsuarioModelo usuarioExiste = usuario.get();

                if (jwtService.validarToken(token, usuarioExiste)) {

                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + usuarioExiste.getRol().name());

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            usuarioExiste,
                            null,
                            List.of(authority)
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }


        filterChain.doFilter(request, response);
    }

    private String getTokenFromReques(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
