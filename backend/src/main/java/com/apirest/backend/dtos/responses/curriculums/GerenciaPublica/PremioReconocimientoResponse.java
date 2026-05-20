package com.apirest.backend.dtos.responses.curriculums.GerenciaPublica;

import com.apirest.backend.models.enums.Curriculum.TipoPremioReconocimientoCurriculum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PremioReconocimientoResponse {
    private String id;
    private TipoPremioReconocimientoCurriculum tipo;
    private String nombreEntidadOrganizacion;
    private Instant fecha;
    private String pais;
    private String departamento;
    private String municipio;
}
