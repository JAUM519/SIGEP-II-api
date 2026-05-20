package com.apirest.backend.repositories;

import com.apirest.backend.models.curriculum.CurriculumModelo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ICurriculumRepository extends MongoRepository <CurriculumModelo, String> {
    Optional<CurriculumModelo> findByUsuarioId(String usuarioId);
}
