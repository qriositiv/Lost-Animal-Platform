package com.straysafe.backend.repository;

import com.straysafe.backend.domain.ListReportDAOResponse;
import com.straysafe.backend.domain.ReportDAORequest;
import com.straysafe.backend.domain.ReportDAOResponse;
import com.straysafe.backend.domain.SimilarityReportDAORequest;
import com.straysafe.backend.domain.SimilarityReportDAOResponse;
import com.straysafe.backend.repository.mapper.ReportListMapper;
import com.straysafe.backend.repository.mapper.ReportMapper;
import com.straysafe.backend.util.enums.ReportStatus;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class ReportRepository implements ReportRepositoryInterface {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final RestTemplate restTemplate;

  @Value("${PYTHON_IP:127.0.0.1:5000}")
  private String python_ip;

  public static final int PAGE_SIZE = 20;


  @Autowired
  public ReportRepository(
      NamedParameterJdbcTemplate template, RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
    this.namedParameterJdbcTemplate = template;
  }

  @Override
  public void createReport(ReportDAORequest request) {
    String query =
        """
INSERT INTO Report (report_id, user_id, pet_id, report_type, report_status, address, latitude, longitude, note)
VALUES (:reportId, :userId, :petId, :reportTypeId, :reportStatusId, :address, :latitude, :longitude, :note);
""";

    SqlParameterSource params =
        new MapSqlParameterSource()
            .addValue("reportId", request.reportId())
            .addValue("userId", request.userId())
            .addValue("petId", request.petId())
            .addValue("reportTypeId", request.reportType().toString())
            .addValue("reportStatusId", request.reportStatus().toString())
            .addValue("address", request.address())
            .addValue("latitude", request.latitude())
            .addValue("longitude", request.longitude())
            .addValue("note", request.note());

    namedParameterJdbcTemplate.update(query, params);
  }

  @Override
  public List<ReportDAOResponse> getAllReports() {
    String query =
        """
SELECT report_id, user_id, pet_id, report_type, report_status, address, latitude, longitude, note, created_at
FROM Report
WHERE report_status = 'ACTIVE'
ORDER BY created_at DESC;
""";
    return namedParameterJdbcTemplate.query(query, new ReportMapper());
  }

  public List<ReportDAOResponse> getAllReportsByUserId(String userId) {
    String query =
        """
SELECT report_id, user_id, pet_id, report_type, report_status, address, latitude, longitude, note, created_at
FROM Report
WHERE report_status = 'ACTIVE'
AND user_id = :userId
ORDER BY created_at DESC;
""";
    SqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);
    return namedParameterJdbcTemplate.query(query, params, new ReportMapper());
  }

  @Override
  public ReportDAOResponse getReportById(String reportId) {
    String query =
        """
SELECT report_id, user_id, pet_id, report_type, report_status, address, latitude, longitude, note, created_at
FROM Report
WHERE report_id = :reportId;
""";
    SqlParameterSource params = new MapSqlParameterSource().addValue("reportId", reportId);
    return namedParameterJdbcTemplate.queryForObject(query, params, new ReportMapper());
  }

  @Override
  public void deleteReportById(String reportId) {
    String query =
        """
        UPDATE Report
        SET report_status = :status
        WHERE report_id = :reportId;
        """;
    SqlParameterSource params =
        new MapSqlParameterSource()
            .addValue("reportId", reportId)
            .addValue("status", ReportStatus.DELETED.toString());
    namedParameterJdbcTemplate.update(query, params);
  }

  @Override
  public String getOwnerByReportId(String reportId) {
    String query =
        """
        SELECT user_id
        FROM Report
        WHERE report_id = :reportId;
        """;
    SqlParameterSource params = new MapSqlParameterSource().addValue("reportId", reportId);
    return namedParameterJdbcTemplate.queryForObject(query, params, String.class);
  }

  @Override
  public void updateReportById(String reportId) {
    String query =
        """
        UPDATE Report
        SET report_status = :status
        WHERE report_id = :reportId;
        """;
    SqlParameterSource params =
        new MapSqlParameterSource()
            .addValue("reportId", reportId)
            .addValue("status", ReportStatus.FOUND.toString());
    namedParameterJdbcTemplate.update(query, params);
  }

  public void blockReportById(String reportId) {
    String query =
        """
        UPDATE Report
        SET report_status = :status
        WHERE report_id = :reportId;
        """;
    SqlParameterSource params =
        new MapSqlParameterSource()
            .addValue("reportId", reportId)
            .addValue("status", ReportStatus.BLOCKED.toString());
    namedParameterJdbcTemplate.update(query, params);
  }

  @Override
  public List<SimilarityReportDAOResponse> getSimilarReportList(
      SimilarityReportDAORequest similarReportDAORequest) {
    String url = "http://" + python_ip + "/public/quick-compare";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity<List<SimilarityReportDAOResponse>> reportSimilarityResponse =
        restTemplate.exchange(
            url,
            HttpMethod.POST,
            new HttpEntity<>(similarReportDAORequest, headers),
            new ParameterizedTypeReference<>() {});
    return reportSimilarityResponse.getBody();
  }

  public ListReportDAOResponse getReportByIdImageComparison(String reportId) {
    String query =
        """
        SELECT
            u.id AS user_id,
            r.report_id,
            p.pet_id,
            p.pet_name,
            t.type_name AS pet_type,
            b.breed_name AS pet_breed,
            p.pet_gender,
            p.pet_size,
            p.pet_age,
            img.image AS pet_image,
            r.report_type,
            r.address,
            r.latitude,
            r.longitude,
            r.note,
            r.created_at
        FROM
            Report r
        JOIN
            "User" u ON r.user_id = u.id
        JOIN
            Pet p ON r.pet_id = p.pet_id
        JOIN
            Type t ON p.pet_type = t.type_id
        LEFT JOIN
            Breed b ON p.pet_breed = b.breed_id
        LEFT JOIN
            Image img ON p.pet_id = img.image_id
        WHERE
            r.report_id = :report_id;
        """;
    SqlParameterSource params = new MapSqlParameterSource().addValue("report_id", reportId);

    return namedParameterJdbcTemplate.queryForObject(query, params, new ReportListMapper());
  }

  public List<ListReportDAOResponse> getListReportWithFilters(
      String query, Map<String, Object> params) {
    return namedParameterJdbcTemplate.query(
        query, new MapSqlParameterSource(params), new ReportListMapper());
  }
}
