package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.UserCredentialDAOResponse;
import com.straysafe.backend.util.enums.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserCredentialsMapper implements RowMapper<UserCredentialDAOResponse> {
    @Override
    public UserCredentialDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {

        return new UserCredentialDAOResponse(
                resultSet.getString("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("email"),
                resultSet.getString("telephone"),
                Role.valueOf(resultSet.getString("role")));
    }
}
