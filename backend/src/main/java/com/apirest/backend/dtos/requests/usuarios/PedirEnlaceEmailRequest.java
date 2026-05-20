package com.apirest.backend.dtos.requests.usuarios;

import com.apirest.backend.models.enums.Usuario.TipoIdentificacionUsuarios;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class PedirEnlaceEmailRequest {
    @NotNull
    private TipoIdentificacionUsuarios tipoIdentificacion;
    @NotBlank
    private String numeroIdentificacion;
}
