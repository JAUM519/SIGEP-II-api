package com.apirest.backend.services;

import com.apirest.backend.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
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

    private final long maxSizeBytes;
    private final String accountId;
    private final String accessKeyId;
    private final String secretAccessKey;
    private final String bucketName;
    private final String publicUrl;
    private final String endpoint;

    public FileStorageService(
            @Value("${app.upload.max-size-bytes:10485760}") long maxSizeBytes,
            @Value("${cloudflare.r2.account-id:}") String accountId,
            @Value("${cloudflare.r2.access-key-id:}") String accessKeyId,
            @Value("${cloudflare.r2.secret-access-key:}") String secretAccessKey,
            @Value("${cloudflare.r2.bucket-name:}") String bucketName,
            @Value("${cloudflare.r2.public-url:}") String publicUrl,
            @Value("${cloudflare.r2.endpoint:}") String endpoint
    ) {
        this.maxSizeBytes = maxSizeBytes;
        this.accountId = clean(accountId);
        this.accessKeyId = clean(accessKeyId);
        this.secretAccessKey = clean(secretAccessKey);
        this.bucketName = clean(bucketName);
        this.publicUrl = removeTrailingSlash(clean(publicUrl));
        this.endpoint = removeTrailingSlash(clean(endpoint));
    }

    public StoredFile guardarArchivo(MultipartFile archivo) {
        validarConfiguracionR2();
        validarArchivo(archivo);

        String originalName = StringUtils.cleanPath(archivo.getOriginalFilename() == null ? "archivo" : archivo.getOriginalFilename());
        String extension = obtenerExtension(originalName);
        String storedName = UUID.randomUUID() + "." + extension;
        String contentType = CONTENT_TYPES_BY_EXTENSION.get(extension);

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storedName)
                    .contentType(contentType)
                    .contentLength(archivo.getSize())
                    .build();

            try (S3Client client = s3Client()) {
                client.putObject(request, RequestBody.fromInputStream(archivo.getInputStream(), archivo.getSize()));
            }

            return new StoredFile(storedName, construirUrlRespuesta(storedName), contentType, archivo.getSize());
        } catch (IOException e) {
            throw new FileStorageException("No fue posible leer el documento seleccionado. Intenta nuevamente.", e);
        } catch (S3Exception e) {
            throw new FileStorageException("No fue posible cargar el documento. Revisa la configuración de almacenamiento.", e);
        }
    }

    public LoadedFile cargarArchivo(String nombreArchivo) {
        validarConfiguracionR2();
        String cleanName = limpiarNombreArchivo(nombreArchivo);

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(cleanName)
                    .build();

            ResponseBytes<GetObjectResponse> response;
            try (S3Client client = s3Client()) {
                response = client.getObjectAsBytes(request);
            }
            String contentType = response.response().contentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = obtenerTipoContenido(cleanName);
            }

            return new LoadedFile(cleanName, contentType, response.asByteArray());
        } catch (NoSuchKeyException e) {
            throw new FileStorageException("El documento solicitado no existe o fue retirado.", e);
        } catch (S3Exception e) {
            throw new FileStorageException("No fue posible abrir el documento solicitado.", e);
        }
    }

    public String obtenerTipoContenido(String nombreArchivo) {
        String extension = obtenerExtension(nombreArchivo);
        return CONTENT_TYPES_BY_EXTENSION.getOrDefault(extension, "application/octet-stream");
    }

    private S3Client s3Client() {
        String endpointFinal = !endpoint.isBlank()
                ? endpoint
                : "https://" + accountId + ".r2.cloudflarestorage.com";

        return S3Client.builder()
                .endpointOverride(URI.create(endpointFinal))
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .chunkedEncodingEnabled(false)
                        .build())
                .build();
    }

    private String construirUrlRespuesta(String storedName) {
        if (!publicUrl.isBlank()) {
            return publicUrl + "/" + storedName;
        }
        return "/api/archivos/" + storedName;
    }

    private void validarConfiguracionR2() {
        if (accountId.isBlank() && endpoint.isBlank()) {
            throw new FileStorageException("El almacenamiento de documentos no está configurado. Falta el identificador de la cuenta.");
        }
        if (accessKeyId.isBlank() || secretAccessKey.isBlank() || bucketName.isBlank()) {
            throw new FileStorageException("El almacenamiento de documentos no está configurado correctamente.");
        }
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

    private String limpiarNombreArchivo(String nombreArchivo) {
        String cleanName = StringUtils.cleanPath(nombreArchivo == null ? "" : nombreArchivo);
        if (cleanName.isBlank() || cleanName.contains("..") || cleanName.contains("/") || cleanName.contains("\\")) {
            throw new FileStorageException("Nombre de archivo no válido.");
        }
        return cleanName;
    }

    private String obtenerExtension(String filename) {
        int index = filename.lastIndexOf('.');
        if (index < 0 || index == filename.length() - 1) {
            throw new FileStorageException("El documento debe tener una extensión válida.");
        }
        return filename.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private String removeTrailingSlash(String value) {
        if (value == null || value.isBlank()) return "";
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    public record StoredFile(String nombreArchivo, String url, String tipoContenido, Long tamañoBytes) {}

    public record LoadedFile(String nombreArchivo, String tipoContenido, byte[] contenido) {}
}
