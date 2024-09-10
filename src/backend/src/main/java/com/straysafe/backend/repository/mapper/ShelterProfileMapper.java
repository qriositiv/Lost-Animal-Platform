package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.ShelterProfileDAOResponse;
import com.straysafe.backend.domain.UserProfileDAOResponse;
import com.straysafe.backend.util.enums.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ShelterProfileMapper implements RowMapper<ShelterProfileDAOResponse> {

    public ShelterProfileDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ShelterProfileDAOResponse(
                resultSet.getString("shelter_id"),
                resultSet.getString("shelter_name"),
                resultSet.getString("shelter_address"),
                resultSet.getString("shelter_latitude"),
                resultSet.getString("shelter_longitude"),
                resultSet.getString("user_id"),
                resultSet.getString("username"),
                resultSet.getString("email"),
                resultSet.getString("telephone"),
                Role.valueOf(resultSet.getString("role")),
                resultSet.getObject("created_at", LocalDateTime.class));
    }
}
