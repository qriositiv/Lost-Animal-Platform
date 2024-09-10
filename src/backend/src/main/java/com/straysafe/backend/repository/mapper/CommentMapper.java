package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.CommentDAOResponse;
import com.straysafe.backend.util.enums.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class CommentMapper implements RowMapper<CommentDAOResponse> {

    @Override
    public CommentDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new CommentDAOResponse(
                resultSet.getString("comment_id"),
                resultSet.getString("report_id"),
                resultSet.getString("user_id"),
                resultSet.getString("username"),
                Role.valueOf(resultSet.getString("role")),
                resultSet.getString("comment"),
                resultSet.getObject("updated_at", LocalDateTime.class),
                resultSet.getObject("created_at", LocalDateTime.class)
        );
    }

}