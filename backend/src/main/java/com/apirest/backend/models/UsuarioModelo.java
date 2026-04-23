package com.apirest.backend.models;

import com.apirest.backend.models.enums.RolUsuarios;
import com.apirest.backend.models.enums.TipoIdentificacionUsuarios;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioModelo {
    @Id
    private ObjectId id;
    private TipoIdentificacionUsuarios tipoIdentificacion;
    private String numeroIdentificacion;
    private String contraseña;
    private String email;
    private RolUsuarios rol;
    private boolean estadoActivo;
    private Instant fechaFin;

}
