package com.straysafe.backend.repository;

import com.straysafe.backend.domain.PetDAORequest;
import com.straysafe.backend.domain.PetDAOResponse;
import com.straysafe.backend.repository.mapper.PetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PetRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public String createPet(PetDAORequest petDAORequest) {

        String query = """
                INSERT INTO Pet (pet_id, user_id, pet_name, pet_type, pet_breed, pet_gender, pet_size, pet_age)
                VALUES (:petId, :userId, :petName, :petType, :petBreed, :petGender, :petSize, :petAge)
                RETURNING pet_id;
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("petId", petDAORequest.petId())
                .addValue("userId", petDAORequest.userId())
                .addValue("petName", petDAORequest.petName())
                .addValue("petType", petDAORequest.petTypeId())
                .addValue("petBreed", petDAORequest.breedId())
                .addValue("petGender", petDAORequest.petGender())
                .addValue("petSize", petDAORequest.petSize())
                .addValue("petAge", petDAORequest.petAge());

        return namedParameterJdbcTemplate.queryForObject(query, params, String.class);
    }

    public List<PetDAOResponse> getAllPets() {
        String query = """
                SELECT pet_id, user_id, pet_name, pet_type, pet_breed, pet_gender, pet_size, pet_age, created_at
                FROM Pet;
                """;
        return namedParameterJdbcTemplate.query(query, new PetMapper());
    }

    public List<PetDAOResponse> getUserPets(String username) {
        String query = """
                SELECT p.pet_id, p.user_id, p.pet_name, p.pet_type, p.pet_breed, p.pet_gender, p.pet_size, p.pet_age, p.created_at
                FROM Pet p
                JOIN "User" u ON p.user_id = u.id
                WHERE u.username = :username;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", username);

        return namedParameterJdbcTemplate.query(query, params, new PetMapper());
    }

    public PetDAOResponse getPetById(String petId) {
        String query = """
                SELECT pet_id, user_id, pet_name, pet_type, pet_breed, pet_gender, pet_size, pet_age, created_at
                FROM Pet
                WHERE pet_id = :petId;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("petId", petId);

        return namedParameterJdbcTemplate.queryForObject(query, params, new PetMapper());
    }

    public void deletePet(String petId) {
        String query = """
                UPDATE Pet
                SET user_id = NULL
                WHERE pet_id = :petId;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("petId", petId);

        namedParameterJdbcTemplate.update(query, params);
    }

}
