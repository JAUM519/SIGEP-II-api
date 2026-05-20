package com.apirest.backend.dtos.requests.usuarios;

import com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class NuevoUsuarioRequest {
    @NotNull
    private TipoIdentificacionUsuarios tipoIdentificacion;
    @NotBlank
    private String numeroIdentificacion;
    @Email
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9@#*!]+$", message = "Caracteres no permitidos en la contraseña")
    private String contraseña;
}
