package com.straysafe.backend.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public ImageRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public void saveImage(String imagePath, String imageId) {

    String query = """
        INSERT INTO Image(image_id, image)
        VALUES(:imageId, :imagePath);
        """;
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("imageId", imageId)
        .addValue("imagePath", imagePath);

    namedParameterJdbcTemplate.update(query, params);
  }

  public String getImagePathById(String imageId) {
    String query = """
        SELECT image
        FROM Image
        WHERE image_id = :imageId;
        """;
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("imageId", imageId);

    return namedParameterJdbcTemplate.queryForObject(query, params, String.class);
  }

}
