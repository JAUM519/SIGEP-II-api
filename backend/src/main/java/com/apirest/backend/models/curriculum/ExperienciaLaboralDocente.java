package com.apirest.backend.models.curriculum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExperienciaLaboralDocente {
    @id
    private String id;
    private String tipoInstitucion;
    private String nombreInstitucion;
    private String pais;
    private String departamento;
    private String municipio;
    private String nivelAcademico;
    private String areaConocimiento;
    private String tipoZona;
    private String trabajoActual;
    private Instant fechaIngreso;
    private Instant fechaTerminacion;
    private String jornadaLaboral;
    private Integer horasPromedioMes;
    private String motivoRetiro;
    private String telefono;
    private String materiaImpartida;
    private Integer tiempoExperiencia;
    private String certificadoLaboral;
    private boolean documentoVerificado;
}
