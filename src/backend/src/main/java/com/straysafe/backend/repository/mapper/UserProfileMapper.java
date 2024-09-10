package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.UserProfileDAOResponse;
import com.straysafe.backend.util.enums.Role;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserProfileMapper implements RowMapper<UserProfileDAOResponse> {

  @Override
  public UserProfileDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    return new UserProfileDAOResponse(
        resultSet.getString("id"),
        resultSet.getString("first_name"),
        resultSet.getString("last_name"),
        resultSet.getString("username"),
        resultSet.getString("email"),
        resultSet.getString("telephone"),
        Role.valueOf(resultSet.getString("role")),
        resultSet.getLong("total_user_report_count"),
        resultSet.getLong("total_user_comment_count"),
        resultSet.getObject("created_at", LocalDateTime.class));
  }
}
