package com.straysafe.backend.repository;

import com.fasterxml.uuid.Generators;
import com.straysafe.backend.api.model.request.UserRegisterRequest;
import com.straysafe.backend.domain.UserCredentialDAOResponse;
import com.straysafe.backend.repository.mapper.UserCredentialsMapper;
import com.straysafe.backend.util.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserAuthRepository implements UserAuthRepositoryInterface {

    PasswordEncoder passwordEncoder;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserAuthRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, PasswordEncoder passwordEncoder) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserCredentialDAOResponse> getUserByLogin(String login) {
        String query = """
                SELECT id, first_name, last_name, username, password, email, telephone, role
                FROM "User"
                WHERE username = :login
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("login", login);
        return namedParameterJdbcTemplate.query(query, params, new UserCredentialsMapper())
                .stream()
                .findFirst();
    }

    @Override
    public UserCredentialDAOResponse registerUser(UserRegisterRequest userRegisterRequest) {

        String generatedUUID = Generators.timeBasedEpochGenerator().generate().toString();

        String query = """
                INSERT INTO "User"(id, first_name, last_name, username, password, email, telephone, role)
                VALUES(:id, :firstName, :lastName, :login, :password, :email, :telephone, :role)
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", generatedUUID)
                .addValue("firstName", userRegisterRequest.firstName())
                .addValue("lastName", userRegisterRequest.lastName())
                .addValue("login", userRegisterRequest.login())
                .addValue("password", passwordEncoder.encode(userRegisterRequest.password()))
                .addValue("email", userRegisterRequest.email())
                .addValue("telephone", userRegisterRequest.phone())
                .addValue("role", Role.USER.toString());


        namedParameterJdbcTemplate.update(query, params);
        return getUserByLogin(userRegisterRequest.login()).orElseThrow();
    }
}

