package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.MapMarkerDAOResponse;
import com.straysafe.backend.domain.MapShelterDAOResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapShelterMapper implements RowMapper<MapShelterDAOResponse> {

    @Override
    public MapShelterDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new MapShelterDAOResponse(
                resultSet.getString("shelter_id"),
                resultSet.getBigDecimal("shelter_latitude"),
                resultSet.getBigDecimal("shelter_longitude")
        );
    }

}
