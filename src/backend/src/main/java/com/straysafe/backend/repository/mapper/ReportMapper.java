package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.ReportDAOResponse;
import com.straysafe.backend.util.enums.ReportStatus;
import com.straysafe.backend.util.enums.ReportType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportMapper implements RowMapper<ReportDAOResponse> {

    @Override
    public ReportDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ReportDAOResponse(
                resultSet.getString("report_id"),
                resultSet.getString("user_id"),
                resultSet.getString("pet_id"),
                ReportType.valueOf(resultSet.getString("report_type")),
                ReportStatus.valueOf(resultSet.getString("report_status")),
                resultSet.getString("address"),
                resultSet.getBigDecimal("latitude"),
                resultSet.getBigDecimal("longitude"),
                resultSet.getString("note"),
                resultSet.getObject("created_at", LocalDateTime.class)
        );
    }
}

