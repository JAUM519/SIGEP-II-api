package com.apirest.backend.dtos.requests.curriculums.Educacion;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActualizarIdiomaRequest {

    private String idiomaId;
    @Size(max = 150)
    private String certificado;
}
