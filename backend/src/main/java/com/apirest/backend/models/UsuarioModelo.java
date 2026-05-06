package com.apirest.backend.models;

import com.apirest.backend.models.enums.RolUsuarios;
import com.apirest.backend.models.enums.TipoIdentificacionUsuarios;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioModelo {
    @Id
    private String id;
    private TipoIdentificacionUsuarios tipoIdentificacion;
    private String numeroIdentificacion;
    private String contraseña;
    private String email;
    private RolUsuarios rol;
    private boolean estadoActivo;
    private Instant fechaFin;

}
