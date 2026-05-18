package com.apirest.backend.models.curriculum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienciaLaboral {
    @id
    private String id;
    private String tipoEntidad;
    private String nombreEntidad;
    private String pais;
    private String departamento;
    private String municipio;
    private String direccionEntidad;
    private String depedencia;
    private String nivelJerarquiaEmpleo;
    private String cargo;
    private String telefono;
    private String trabajoActual;
    private Instant fechaIngreso;
    private Instant fechaRetiro;
    private String jornadaLaboral;
    private Integer horasPromedioMes;
    private Integer tiempoExperiencia;
    private String motivoRetiro;
    private String certificadoLaboral;
    private String DocumentoVerificado;

}
