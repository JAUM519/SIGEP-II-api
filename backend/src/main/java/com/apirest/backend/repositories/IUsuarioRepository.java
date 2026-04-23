package com.apirest.backend.repositories;

import com.apirest.backend.models.UsuarioModelo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IUsuarioRepository extends MongoRepository<UsuarioModelo, ObjectId> {
}
