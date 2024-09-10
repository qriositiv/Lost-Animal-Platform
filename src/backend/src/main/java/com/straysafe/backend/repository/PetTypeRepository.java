package com.straysafe.backend.repository;

import com.straysafe.backend.util.enums.PetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class PetTypeRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public PetTypeRepository(NamedParameterJdbcTemplate template) {
    this.namedParameterJdbcTemplate = template;
  }

  public int getIdByType(PetType petType) {
    String query = """
        SELECT type_id FROM Type WHERE type_name = :type;
        """;
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("type", petType.toString());

    return namedParameterJdbcTemplate.queryForObject(query, params, (rs, rowNum) -> rs.getInt("type_id"));
  }

  public PetType getTypeById(int id) {
    String query = """
        SELECT type_name FROM Type WHERE type_id = :id;
        """;
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("id", id);

    return namedParameterJdbcTemplate.queryForObject(query, params,
        (rs, rowNum) -> PetType.valueOf(rs.getString("type_name")));
  }

}
