package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.MapMarkerDAOResponse;
import com.straysafe.backend.util.enums.ReportType;
import org.springframework.jdbc.core.RowMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class MapMarkersMapper implements RowMapper<MapMarkerDAOResponse> {
    @Override
    public MapMarkerDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new MapMarkerDAOResponse(
                resultSet.getString("report_id"),
                resultSet.getBigDecimal("latitude"),
                resultSet.getBigDecimal("longitude"),
                ReportType.valueOf(resultSet.getString("report_type")),
                resultSet.getInt("pet_type")
        );
    }
}