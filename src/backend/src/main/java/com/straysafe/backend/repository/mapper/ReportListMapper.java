package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.api.model.response.PetResponse;
import com.straysafe.backend.domain.ListReportDAOResponse;
import com.straysafe.backend.util.enums.PetAge;
import com.straysafe.backend.util.enums.PetGender;
import com.straysafe.backend.util.enums.PetSize;
import com.straysafe.backend.util.enums.PetType;
import com.straysafe.backend.util.enums.ReportType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportListMapper implements RowMapper<ListReportDAOResponse> {

    @Override
    public ListReportDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new ListReportDAOResponse(
                resultSet.getString("user_id"),
                resultSet.getString("report_id"),
                new PetResponse(
                        resultSet.getString("pet_name"),
                        PetType.valueOf(resultSet.getString("pet_type")),
                        resultSet.getString("pet_breed"),
                        PetGender.valueOf(resultSet.getString("pet_gender")),
                        PetSize.valueOf(resultSet.getString("pet_size")),
                        PetAge.valueOf(resultSet.getString("pet_age")),
                        resultSet.getString("pet_image")
                ),
                ReportType.valueOf(resultSet.getString("report_type")),
                resultSet.getString("address"),
                resultSet.getBigDecimal("latitude"),
                resultSet.getBigDecimal("longitude"),
                resultSet.getString("note"),
                resultSet.getObject("created_at", LocalDateTime.class)
        );
    }
}
