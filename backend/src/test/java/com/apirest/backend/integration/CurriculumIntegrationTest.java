package com.apirest.backend.integration;

import com.apirest.backend.models.curriculum.CurriculumModelo;
import com.apirest.backend.repositories.ICurriculumRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Falla por configuración de MongoDB embebido - se arreglará después")
@DataMongoTest
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
class CurriculumIntegrationTest {

    @Autowired
    private ICurriculumRepository curriculumRepository;

    @Test
    void testSaveAndFind() {
        CurriculumModelo curriculum = new CurriculumModelo();
        curriculum.setUsuarioId("123");
        curriculumRepository.save(curriculum);

        Optional<CurriculumModelo> found = curriculumRepository.findByUsuarioId("123");
        assertTrue(found.isPresent());
    }
}