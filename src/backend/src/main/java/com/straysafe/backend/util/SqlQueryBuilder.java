package com.straysafe.backend.util;

import com.straysafe.backend.api.model.request.GridviewReportRequest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlQueryBuilder {

    private SqlQueryBuilder() {
        throw new IllegalStateException("Utility class");
    }
    public static String buildQuery(GridviewReportRequest request, Map<String, Object> params) {
        int limit = 20;
        long offset = 10 * (request.page() - 1);

        boolean isReferenceLocationProvided = (request.reportLocationRequest() != null && request.reportLocationRequest().longitude() != null
                                               && request.reportLocationRequest().latitude() != null && request.reportLocationRequest().distance() != null);

        StringBuilder sql = new StringBuilder("""
                WITH ReportWithDistance AS (
                    SELECT
                     r.report_id,
                     r.user_id,
                     r.pet_id,
                     r.report_type,
                     r.report_status,
                     r.address,
                     r.latitude,
                     r.longitude,
                     r.note,
                     r.created_at,
                     r.updated_at,
                     p.pet_name,
                     t.type_name AS pet_type,
                     b.breed_name AS pet_breed,
                     p.pet_gender,
                     p.pet_size,
                     p.pet_age,
                     img.image AS pet_image
                """);

        if (isReferenceLocationProvided) {
            sql.append("""
                     , COALESCE(
                            (6371 * ACOS(
                                      LEAST(1, GREATEST(-1, COS(RADIANS(:referenceLatitude)) * COS(RADIANS(r.latitude)) * COS(RADIANS(r.longitude) - RADIANS(:referenceLongitude)))
                                          + SIN(RADIANS(:referenceLatitude)) * SIN(RADIANS(r.latitude))
                                      ))
                                  ),
                                  0) AS distance
                     """);
            params.put("referenceLatitude", request.reportLocationRequest().latitude());
            params.put("referenceLongitude", request.reportLocationRequest().longitude());
        }

        sql.append("""
                     FROM
                     Report r
                          JOIN
                            Pet p ON r.pet_id = p.pet_id
                          JOIN
                            Type t ON p.pet_type = t.type_id
                          LEFT JOIN
                            Breed b ON p.pet_breed = b.breed_id
                          LEFT JOIN
                            Image img ON p.pet_id = img.image_id
                          JOIN
                            "User" u ON r.user_id = u.id
                          WHERE
                            r.report_status = 'ACTIVE'
                """);

        if (request.reportTypeList() != null) {
            List<String> reportTypeList = convertEnumListToStringList(request.reportTypeList());
            appendCondition(sql, "r.report_type", reportTypeList, params, "reportTypeList");
        }
        if (request.reportPublishHourRange() != null) {
            String hourRange = convertHoursToString(request.reportPublishHourRange());
            sql.append(" AND ").append("r.created_at >= NOW() - INTERVAL '").append(hourRange).append("'");
            params.put("reportPublishHourRange", hourRange);
        }
        if (request.petDataRequest() != null) {
            if (request.petDataRequest().petTypeList() != null) {
                List<String> petTypeList = convertEnumListToStringList(request.petDataRequest().petTypeList());
                appendCondition(sql, "t.type_name", petTypeList, params, "petTypeList");
            }
            if (request.petDataRequest().petBreedList() != null) {
                appendCondition(sql, "b.breed_name", request.petDataRequest().petBreedList(), params, "petBreedList");
            }
            if (request.petDataRequest().petGenderList() != null) {
                List<String> petGenderList = convertEnumListToStringList(request.petDataRequest().petGenderList());
                appendCondition(sql, "p.pet_gender", petGenderList, params, "petGenderList");
            }
        }

        if (request.reportCreator() != null) {
            sql.append(" AND ").append("u.username").append(" ILIKE ").append(":reportCreator");
            params.put("reportCreator", "%"+request.reportCreator()+"%");
        }

        sql.append(")");

        if (isReferenceLocationProvided) {
            sql.append("""
                    SELECT *
                    FROM ReportWithDistance
                    WHERE distance < :distance
                    ORDER BY created_at DESC
                    LIMIT :limit
                    OFFSET :offset;
                    """);
            params.put("distance", request.reportLocationRequest().distance());
        } else {
            sql.append("""
                    SELECT *
                    FROM ReportWithDistance
                    ORDER BY created_at DESC
                    LIMIT :limit
                    OFFSET :offset;
                    """);
        }

        params.put("limit", limit);
        params.put("offset", offset);

        return sql.toString();
    }

    public static void appendCondition(StringBuilder sql, String column, Object value, Map<String, Object> params, String paramName) {
        if (value != null) {
            if (value instanceof List && !((List<?>) value).isEmpty()) {
                sql.append(" AND ").append(column).append(" IN (:").append(paramName).append(")");
                params.put(paramName, value);
            } else if (!(value instanceof List)) {
                sql.append(" AND ").append(column).append(" = :").append(paramName);
                params.put(paramName, value);
            }
        }
    }
    private static <E extends Enum<E>> List<String> convertEnumListToStringList(List<E> enumList) {
        return enumList.stream().map(Enum::name).collect(Collectors.toList());
    }

    private static String convertHoursToString(Integer hours) {
        return hours + "h";
    }
}
