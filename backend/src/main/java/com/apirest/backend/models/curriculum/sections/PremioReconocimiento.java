package com.apirest.backend.models.curriculum.sections;

import com.apirest.backend.models.enums.Curriculum.TipoPremioReconocimientoCurriculum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PremioReconocimiento {
    @Id
    private String id;
    private TipoPremioReconocimientoCurriculum tipo;
    private String nombreEntidadOrganizacion;
    private Instant fecha;
    private String pais;
    private String departamento;
    private String municipio;
}
