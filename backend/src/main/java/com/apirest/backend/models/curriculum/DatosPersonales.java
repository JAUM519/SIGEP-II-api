package com.apirest.backend.models.curriculum;

import com.apirest.backend.models.curriculum.sections.DatosBasicos;
import com.apirest.backend.models.curriculum.sections.DatosContacto;
import com.apirest.backend.models.curriculum.sections.DatosDemograficos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatosPersonales {

    private DatosBasicos datosBasicos;
    private DatosDemograficos datosDemograficos;
    private DatosContacto datosContacto;

}
