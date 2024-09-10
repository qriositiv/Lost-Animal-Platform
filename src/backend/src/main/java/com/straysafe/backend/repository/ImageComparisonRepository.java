package com.straysafe.backend.repository;

import com.straysafe.backend.domain.ImageComparisonDAORequest;
import com.straysafe.backend.domain.ImageComparisonDAOResponse;
import com.straysafe.backend.repository.mapper.ImageComparisonMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageComparisonRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ImageComparisonRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<ImageComparisonDAOResponse> getRequiredReportIds(ImageComparisonDAORequest imageComparisonDAORequest){

        String query = """

                SELECT r.report_id ,r.pet_id,
                r.distance
                FROM (
                    SELECT report_id,
                           pet_id,
                           latitude,
                           longitude,
                           (6371 * ACOS(
                               LEAST(1, GREATEST(-1, COS(RADIANS(54.6872)) * COS(RADIANS(latitude)) * COS(RADIANS(longitude) - RADIANS(25.2797))
                                         + SIN(RADIANS(54.6872)) * SIN(RADIANS(latitude))
                               ))
                           )) AS distance
                    FROM Report
                    WHERE report_type in ('SEEN','TAKEN')
                    AND report_status = 'ACTIVE'
                ) AS r
                JOIN Pet p ON r.pet_id = p.pet_id
                JOIN Type t ON p.pet_type = t.type_id
                WHERE distance < 20
                AND t.type_name = :pet_type
                ORDER BY distance;
                """;

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("reference_latitude",imageComparisonDAORequest.latitude())
                .addValue("reference_longitude",imageComparisonDAORequest.longitude())
                .addValue("pet_type",imageComparisonDAORequest.petType());

        return namedParameterJdbcTemplate.query(query, params, new ImageComparisonMapper());
    }

}
