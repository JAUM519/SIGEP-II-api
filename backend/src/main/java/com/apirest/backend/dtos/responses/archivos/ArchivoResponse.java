package com.apirest.backend.dtos.responses.archivos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchivoResponse {
    private String nombreArchivo;
    private String url;
    private String tipoContenido;
    private Long tamañoBytes;
}
