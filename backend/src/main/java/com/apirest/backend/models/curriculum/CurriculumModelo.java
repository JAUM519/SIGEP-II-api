package com.apirest.backend.models.curriculum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("curriculums")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumModelo {
    @Id
    private ObjectId id;
    private ObjectId usuarioId;
    private DatosPersonales datosPersonales;
}
