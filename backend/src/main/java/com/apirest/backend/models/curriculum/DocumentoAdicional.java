package com.apirest.backend.models.curriculum;

import com.apirest.backend.models.enums.Curriculum.DescripcionDocumentoAdicionalCurriculum;
import com.apirest.backend.models.enums.Curriculum.TipoDocumentoCurriculum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoAdicional {
    @Id
    private String id;
    private TipoDocumentoCurriculum tipoDocumento;
    private DescripcionDocumentoAdicionalCurriculum descripcion;
    private String documento;
    private Boolean documentoVerificado;
}
