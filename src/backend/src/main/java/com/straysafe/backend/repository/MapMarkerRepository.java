package com.straysafe.backend.repository;

import com.straysafe.backend.domain.MapMarkerDAOResponse;
import com.straysafe.backend.domain.MapShelterDAOResponse;
import com.straysafe.backend.repository.mapper.MapMarkersMapper;
import com.straysafe.backend.repository.mapper.MapShelterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MapMarkerRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public MapMarkerRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public List<MapMarkerDAOResponse> getMapMarker(){
        String query = """
                SELECT Report.report_id, Report.report_type, Report.latitude, Report.longitude, Pet.pet_type
                FROM Report
                JOIN Pet ON Report.pet_id = Pet.pet_id
				JOIN "User" ON Report.user_id = "User".id
                WHERE Report.report_status = 'ACTIVE'
				AND "User".role <> 'SHELTER';
                """;

        return namedParameterJdbcTemplate.query(query, new MapMarkersMapper());
    }

    public List<MapShelterDAOResponse> getShelterMarker(){
        String query = """
                SELECT shelter_id, shelter_latitude, shelter_longitude
                FROM Shelter;
                """;

        return namedParameterJdbcTemplate.query(query, new MapShelterMapper());
    }

}
