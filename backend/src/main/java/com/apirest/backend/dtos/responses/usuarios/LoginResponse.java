package com.apirest.backend.dtos.responses.usuarios;

import com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private TipoIdentificacionUsuarios tipoIdentificacion;
    private String numeroIdentificacion;
    private String token;

}
