package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.UserDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserReportInfoMapper implements RowMapper<UserDAOResponse> {

    @Override
    public UserDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new UserDAOResponse(
                resultSet.getString("username"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("email"),
                resultSet.getString("telephone"));
    }
}
