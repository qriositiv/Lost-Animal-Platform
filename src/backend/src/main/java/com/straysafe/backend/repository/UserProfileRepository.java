package com.straysafe.backend.repository;

import com.straysafe.backend.domain.ProfileUpdateDAORequest;
import com.straysafe.backend.domain.SecurityUpdateDAORequest;
import com.straysafe.backend.domain.ShelterProfileDAOResponse;
import com.straysafe.backend.repository.mapper.ShelterProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.straysafe.backend.domain.UserProfileDAOResponse;
import com.straysafe.backend.repository.mapper.UserProfileMapper;

@Repository
public class UserProfileRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public UserProfileRepository(NamedParameterJdbcTemplate template) {
    this.namedParameterJdbcTemplate = template;
  }

  public UserProfileDAOResponse getUserProfileById(String username) {
    String query = """
        SELECT
            u.id,
            u.first_name,
            u.last_name,
            u.role,
            u.username,
            u.email,
            u.telephone,
            u.created_at,
            COALESCE(user_reports.report_count, 0) AS total_user_report_count,
            COALESCE(user_comments.comment_count, 0) AS total_user_comment_count
        FROM "User" u
        LEFT JOIN (
            SELECT user_id, COUNT(*) AS report_count
            FROM Report
            GROUP BY user_id
        ) user_reports ON u.id = user_reports.user_id
        LEFT JOIN (
            SELECT user_id, COUNT(*) AS comment_count
            FROM Comment
            GROUP BY user_id
        ) user_comments ON u.id = user_comments.user_id
        WHERE u.username = :username;
        """;

    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("username", username);

    return namedParameterJdbcTemplate.queryForObject(query, params, new UserProfileMapper());
  }

  public void updateProfile(ProfileUpdateDAORequest profileUpdateDAORequest) {
    String query = """
        UPDATE "User"
        SET email = :email,
            first_name = :firstName,
            last_name = :lastName,
            telephone = :telephone,
            username = :username
        WHERE id = :user_id
    """;

    SqlParameterSource params = new MapSqlParameterSource()
            .addValue("email", profileUpdateDAORequest.email())
            .addValue("firstName", profileUpdateDAORequest.firstName())
            .addValue("lastName", profileUpdateDAORequest.lastName())
            .addValue("telephone", profileUpdateDAORequest.telephone())
            .addValue("username", profileUpdateDAORequest.username())
            .addValue("user_id", profileUpdateDAORequest.userId());

    namedParameterJdbcTemplate.update(query, params);

  }

  public void updateSecurityAndOther(SecurityUpdateDAORequest securityUpdateDAORequest) {
    String query = """
        UPDATE "User"
        SET password = :password
        WHERE id = :user_id;
    """;

    SqlParameterSource params = new MapSqlParameterSource()
            .addValue("user_id", securityUpdateDAORequest.userId())
            .addValue("password", securityUpdateDAORequest.password());

    namedParameterJdbcTemplate.update(query, params);

  }


  public void blockUser(String userId) {

    String query =
            """
            DELETE FROM Comment
            WHERE report_id IN (SELECT report_id FROM Report WHERE user_id = :userId);
            
            DELETE FROM Notifies
            WHERE report_id IN (SELECT report_id FROM Report WHERE user_id = :userId);
            
            DELETE FROM Report
            WHERE user_id = :userId;
            
            DELETE FROM Image
            WHERE image_id IN (SELECT pet_id FROM Pet WHERE user_id = :userId);
            
            DELETE FROM Pet
            WHERE user_id = :userId;
            
            DELETE FROM Shelter
            WHERE user_id = :userId;
            
            DELETE FROM "User"
            WHERE id = :userId;
        """;

    SqlParameterSource params = new MapSqlParameterSource()
            .addValue("userId", userId);

    namedParameterJdbcTemplate.update(query, params);
  }

  public ShelterProfileDAOResponse getShelterById(String shelterId) {
    String query = """
        SELECT
            s.shelter_id,
            s.shelter_name,
            s.shelter_address,
            s.shelter_latitude,
            s.shelter_longitude,
            s.user_id,
            u.username,
            u.email,
            u.telephone,
            u.role,
            u.created_at
        FROM Shelter s
        JOIN "User" u ON s.user_id = u.id
        WHERE s.shelter_id = :shelterId;
        """;

    SqlParameterSource params = new MapSqlParameterSource()
            .addValue("shelterId", shelterId);

    return namedParameterJdbcTemplate.queryForObject(query, params, new ShelterProfileMapper());
  }
}
