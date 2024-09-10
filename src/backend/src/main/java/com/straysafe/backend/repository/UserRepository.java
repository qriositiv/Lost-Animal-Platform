package com.straysafe.backend.repository;

import com.straysafe.backend.domain.UserDAOResponse;
import com.straysafe.backend.repository.mapper.UserReportInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public UserDAOResponse getUserById(String userId) {

        String query = """
        SELECT username, first_name, last_name, email, telephone
        FROM "User"
        WHERE id = :userId;
        """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId);

        return namedParameterJdbcTemplate.queryForObject(query, params, new UserReportInfoMapper());
    }
}
