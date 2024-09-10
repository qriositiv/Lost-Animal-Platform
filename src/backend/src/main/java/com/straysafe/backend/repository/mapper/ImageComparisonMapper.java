package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.ImageComparisonDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageComparisonMapper implements RowMapper<ImageComparisonDAOResponse> {
    @Override
    public ImageComparisonDAOResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ImageComparisonDAOResponse(
                rs.getString("report_id"),
                rs.getString("pet_id")
        );
    }
}