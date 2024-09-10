package com.straysafe.backend.repository.mapper;

import com.straysafe.backend.domain.PetDAOResponse;
import com.straysafe.backend.util.enums.PetAge;
import com.straysafe.backend.util.enums.PetGender;
import com.straysafe.backend.util.enums.PetSize;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class PetMapper  implements RowMapper<PetDAOResponse> {

    @Override
    public PetDAOResponse mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new PetDAOResponse(
                resultSet.getString("pet_id"),
                resultSet.getString("user_id"),
                resultSet.getString("pet_name"),
                resultSet.getInt("pet_type"),
                resultSet.getInt("pet_breed"),
                PetGender.valueOf(resultSet.getString("pet_gender")),
                PetSize.valueOf(resultSet.getString("pet_size")),
                PetAge.valueOf(resultSet.getString("pet_age")),
                resultSet.getObject("created_at", LocalDateTime.class)
        );
    }
}