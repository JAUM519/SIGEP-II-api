package com.apirest.backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String PROPERTY_SOURCE_NAME = "dotenv";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Path dotenvDirectory = findDotenvDirectory(environment);
        if (dotenvDirectory == null) {
            return;
        }

        Dotenv dotenv = Dotenv.configure()
                .directory(dotenvDirectory.toString())
                .filename(".env")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        Map<String, Object> properties = new LinkedHashMap<>();
        for (DotenvEntry entry : dotenv.entries()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (hasText(key) && value != null) {
                properties.put(key, value);
                addCloudflareAlias(properties, key, value);
            }
        }

        if (!properties.isEmpty()) {
            environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
        }
    }

    private Path findDotenvDirectory(ConfigurableEnvironment environment) {
        List<Path> candidates = new ArrayList<>();

        addCandidate(candidates, System.getProperty("DOTENV_DIRECTORY"));
        addCandidate(candidates, System.getenv("DOTENV_DIRECTORY"));
        addCandidate(candidates, environment.getProperty("DOTENV_DIRECTORY"));

        Path cwd = Paths.get("").toAbsolutePath().normalize();
        addCandidate(candidates, cwd.toString());
        addCandidate(candidates, cwd.resolve("backend").toString());
        addCandidate(candidates, cwd.resolve("..").normalize().toString());
        addCandidate(candidates, cwd.resolve("../backend").normalize().toString());

        Path parent = cwd.getParent();
        if (parent != null) {
            addCandidate(candidates, parent.toString());
            addCandidate(candidates, parent.resolve("backend").toString());
        }

        return candidates.stream()
                .distinct()
                .filter(path -> Files.isRegularFile(path.resolve(".env")))
                .findFirst()
                .orElse(null);
    }

    private void addCloudflareAlias(Map<String, Object> properties, String key, String value) {
        switch (key) {
            case "CLOUDFLARE_R2_ACCOUNT_ID" -> properties.put("cloudflare.r2.account-id", value);
            case "CLOUDFLARE_R2_ACCESS_KEY_ID" -> properties.put("cloudflare.r2.access-key-id", value);
            case "CLOUDFLARE_R2_SECRET_ACCESS_KEY" -> properties.put("cloudflare.r2.secret-access-key", value);
            case "CLOUDFLARE_R2_BUCKET_NAME" -> properties.put("cloudflare.r2.bucket-name", value);
            case "CLOUDFLARE_R2_ENDPOINT" -> properties.put("cloudflare.r2.endpoint", value);
            case "CLOUDFLARE_R2_PUBLIC_URL" -> properties.put("cloudflare.r2.public-url", value);
            case "APP_UPLOAD_MAX_SIZE_BYTES" -> properties.put("app.upload.max-size-bytes", value);
            default -> {
                // No alias needed.
            }
        }
    }

    private void addCandidate(List<Path> candidates, String rawPath) {
        if (hasText(rawPath)) {
            candidates.add(Paths.get(rawPath).toAbsolutePath().normalize());
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
