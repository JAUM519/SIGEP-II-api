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
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{6,20}$",
            message = "La contraseña debe contener al menos una letra, un número y un carácter especial")
    private String contraseña;
}
