package com.apirest.backend.models.curriculum.sections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipacionProyecto {
    @Id
    private String id;
    private String nombre;
    private String rolDesempeñado;
    private String nombreEntidadOrganizacion;
    private String pais;
    private String departamento;
    private String municipio;
    private Instant fechaInicio;
    private Instant fechaTerminacion;

}
