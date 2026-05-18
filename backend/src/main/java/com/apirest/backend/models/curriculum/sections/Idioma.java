package com.apirest.backend.models.curriculum.sections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Idioma {
    @id
    private String id;
    private String idioma;
    private Instant fechaCertificado;
    private String conversacion;
}
