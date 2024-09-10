package com.straysafe.backend.repository;

import com.straysafe.backend.util.enums.PetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BreedRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public BreedRepository(NamedParameterJdbcTemplate template) {
    this.namedParameterJdbcTemplate = template;
  }

  public int getIdByTypeAndName(long id, String breedName) {
    String query = """
        SELECT breed_id FROM Breed
        WHERE type_id = :type
        AND breed_name = :breed;
        """;
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("type", id)
        .addValue("breed", breedName);

    return namedParameterJdbcTemplate.queryForObject(query, params, (rs, rowNum) -> rs.getInt("breed_id"));
  }

  public String getBreedById(int id) {
    String query = """
        SELECT breed_name FROM Breed WHERE breed_id = :id;
        """;
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("id", id);

    return namedParameterJdbcTemplate.queryForObject(query, params, (rs, rowNum) -> rs.getString("breed_name"));
  }
}
