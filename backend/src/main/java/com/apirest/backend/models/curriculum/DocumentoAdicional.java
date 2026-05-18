package com.apirest.backend.models.curriculum;

import com.apirest.backend.models.enums.Curriculum.DescripcionDocumentoAdicionalCurriculum;
import com.apirest.backend.models.enums.Curriculum.TipoDocumentoCurriculum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoAdicional {
    @id
    private String id;
    private TipoDocumentoCurriculum tipoDocumento;
    private DescripcionDocumentoAdicionalCurriculum descripcion;
    private String documento;
    private Boolean documentoVerificado;
}
