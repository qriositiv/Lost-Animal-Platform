package com.straysafe.backend.repository;

import com.straysafe.backend.domain.CommentDAORequest;
import com.straysafe.backend.domain.CommentDAOResponse;
import com.straysafe.backend.repository.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentsRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CommentsRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public List<CommentDAOResponse> getCommentsByReportId(String reportId) {
        String query = """
                SELECT
                    c.comment_id,
                    c.report_id,
                    c.user_id,
                    u.username,
                    u.role,
                    c.comment,
                    c.updated_at,
                    c.created_at
                FROM
                    Comment c
                JOIN
                    "User" u ON c.user_id = u.id
                WHERE
                    c.report_id = :reportId
                ORDER BY
                    c.created_at;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("reportId", reportId);
        return namedParameterJdbcTemplate.query(query, params, new CommentMapper());
    }

    public void postCommentUnderReport(CommentDAORequest commentDAORequest) {
        String query = """
                INSERT INTO Comment (comment_id, report_id, user_id, comment, updated_at, created_at)
                VALUES (:commentId, :reportId, :userId, :comment, NOW(), NOW());           
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", commentDAORequest.userId())
                .addValue("reportId", commentDAORequest.reportId())
                .addValue("commentId", commentDAORequest.commentId())
                .addValue("comment", commentDAORequest.comment());

        namedParameterJdbcTemplate.update(query, params);
    }

    public void deleteCommentByReportId(String reportId, String commentId) {
        String query = """
                DELETE FROM Comment
                WHERE report_id = :reportId AND comment_id = :commentId;
            """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("reportId", reportId)
                .addValue("commentId", commentId);
        namedParameterJdbcTemplate.update(query, params);
    }

    public String getOwnerByReportId(String reportId, String commentId) {
        String query = """
                SELECT user_id
                FROM Comment
                WHERE report_id = :reportId
                AND comment_id = :commentId;
                """;
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("reportId", reportId)
                .addValue("commentId", commentId);
        return namedParameterJdbcTemplate.queryForObject(query, params, String.class);
    }
}
