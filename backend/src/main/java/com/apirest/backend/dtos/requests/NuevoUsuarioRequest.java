package com.apirest.backend.dtos.requests;

import com.apirest.backend.models.enums.TipoIdentificacionUsuarios;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class NuevoUsuarioRequest {
    private TipoIdentificacionUsuarios tipoIdentificacion;
    @NotBlank
    private String numeroIdentificacion;
    @Email
    private String email;
    private String contraseña;
}
