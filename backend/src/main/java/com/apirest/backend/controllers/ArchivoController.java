package com.apirest.backend.controllers;

import com.apirest.backend.dtos.responses.archivos.ArchivoResponse;
import com.apirest.backend.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoController {

    private final FileStorageService fileStorageService;

    public ArchivoController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArchivoResponse> subirArchivo(@RequestParam("archivo") MultipartFile archivo) {
        FileStorageService.StoredFile storedFile = fileStorageService.guardarArchivo(archivo);

        ArchivoResponse response = ArchivoResponse.builder()
                .nombreArchivo(storedFile.nombreArchivo())
                .url(storedFile.url())
                .tipoContenido(storedFile.tipoContenido())
                .tamañoBytes(storedFile.tamañoBytes())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{nombreArchivo:.+}")
    public ResponseEntity<Resource> obtenerArchivo(@PathVariable String nombreArchivo) {
        Resource resource = fileStorageService.cargarArchivo(nombreArchivo);
        String contentType = fileStorageService.obtenerTipoContenido(nombreArchivo);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
