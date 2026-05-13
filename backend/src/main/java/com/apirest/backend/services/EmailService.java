package com.apirest.backend.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {
    @Value("${resend.api.key}")
    private String resendApiKey;

    public void enviarEnlaceRecuperacion(String emailDestino, String enlace) {
        RestTemplate restTemplate = new RestTemplate();

         HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

         Map<String, Object> body = new HashMap<>();
        body.put("from", "SIGEP-II <onboarding@resend.dev>");
        body.put("to", List.of(emailDestino));
        body.put("subject", "Recuperación de Contraseña - SIGEP-II");
        body.put("html", "<p>Hola, haz clic en el siguiente enlace para restablecer tu clave:</p>" +
                "<a href='" + enlace + "'>Restablecer Contraseña</a>" +
                "<p>Este enlace expirará en 15 minutos.</p>");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity("https://api.resend.com/emails", request, String.class);
    }
}
