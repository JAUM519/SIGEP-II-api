package com.apirest.backend.services;

import com.apirest.backend.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png");
    private static final Map<String, String> CONTENT_TYPES_BY_EXTENSION = Map.of(
            "pdf", "application/pdf",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png"
    );

    private final Path uploadPath;
    private final long maxSizeBytes;

    public FileStorageService(
            @Value("${app.upload-dir:uploads}") String uploadDir,
            @Value("${app.upload.max-size-bytes:10485760}") long maxSizeBytes
    ) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.maxSizeBytes = maxSizeBytes;

        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new FileStorageException("No fue posible preparar la carpeta de documentos.", e);
        }
    }

    public StoredFile guardarArchivo(MultipartFile archivo) {
        validarArchivo(archivo);

        String originalName = StringUtils.cleanPath(archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename());
        String extension = obtenerExtension(originalName);
        String storedName = UUID.randomUUID() + "." + extension;
        Path destino = uploadPath.resolve(storedName).normalize();

        if (!destino.startsWith(uploadPath)) {
            throw new FileStorageException("Nombre de archivo no válido.");
        }

        try {
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
            return new StoredFile(storedName, "/api/archivos/" + storedName, CONTENT_TYPES_BY_EXTENSION.get(extension), archivo.getSize());
        } catch (IOException e) {
            throw new FileStorageException("No fue posible guardar el documento. Intenta nuevamente.", e);
        }
    }

    public Resource cargarArchivo(String nombreArchivo) {
        String cleanName = StringUtils.cleanPath(nombreArchivo);
        if (cleanName.contains("..") || cleanName.contains("/") || cleanName.contains("\\")) {
            throw new FileStorageException("Nombre de archivo no válido.");
        }

        Path archivoPath = uploadPath.resolve(cleanName).normalize();
        if (!archivoPath.startsWith(uploadPath)) {
            throw new FileStorageException("Nombre de archivo no válido.");
        }

        try {
            Resource resource = new UrlResource(archivoPath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            }
            throw new FileStorageException("El documento solicitado no existe o no se puede leer.");
        } catch (MalformedURLException e) {
            throw new FileStorageException("No fue posible abrir el documento solicitado.", e);
        }
    }

    public String obtenerTipoContenido(String nombreArchivo) {
        String extension = obtenerExtension(nombreArchivo);
        return CONTENT_TYPES_BY_EXTENSION.getOrDefault(extension, "application/octet-stream");
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new FileStorageException("Selecciona un documento para cargar.");
        }

        if (archivo.getSize() > maxSizeBytes) {
            throw new FileStorageException("El documento supera el tamaño permitido de 10 MB.");
        }

        String originalName = StringUtils.cleanPath(archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename());
        String extension = obtenerExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new FileStorageException("Solo se permiten documentos PDF, JPG, JPEG o PNG.");
        }

        String expectedContentType = CONTENT_TYPES_BY_EXTENSION.get(extension);
        String receivedContentType = archivo.getContentType();
        if (receivedContentType != null && !receivedContentType.isBlank() && !receivedContentType.equalsIgnoreCase(expectedContentType)) {
            throw new FileStorageException("El tipo de documento no coincide con el archivo seleccionado.");
        }
    }

    private String obtenerExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index < 0 || index == filename.length() - 1) {
            throw new FileStorageException("El documento debe tener una extensión válida.");
        }
        return filename.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    public record StoredFile(String nombreArchivo, String url, String tipoContenido, Long tamañoBytes) {}
}
