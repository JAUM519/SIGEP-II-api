package com.apirest.backend.jwts;

import com.apirest.backend.models.UsuarioModelo;
import com.apirest.backend.models.enums.RolUsuarios;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.llave}")
    private String llaveScreta;

    @Value("${jwt.expiracion}")
    private long expiracionMinutos;


    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(llaveScreta);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(String usuarioId, RolUsuarios rol, String numeroIdentificacion) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        claims.put("numeroIdentificacion", numeroIdentificacion);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(usuarioId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiracionMinutos))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String getUsuarioIdFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean validarToken(String token, UsuarioModelo usuario) {
        final String usuarioId = getUsuarioIdFromToken(token);
        return (usuarioId.equals(usuario.getId()) && !isExpiradoToken(token));
    }

    private Claims getAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getFechaExpiracion(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isExpiradoToken(String token) {
        return getFechaExpiracion(token).before(new Date());
    }
}
