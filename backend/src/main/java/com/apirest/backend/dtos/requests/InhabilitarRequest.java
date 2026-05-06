package com.apirest.backend.dtos.requests;

import com.apirest.backend.models.enums.TipoIdentificacionUsuarios;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class InhabilitarRequest {
    @NotNull
    private TipoIdentificacionUsuarios tipoIdentificacion;
    @NotBlank
    private String numeroIdentificacion;
    @NotNull
    private Instant fechaFin;
}
