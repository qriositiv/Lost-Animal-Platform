package com.straysafe.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class StatisticRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StatisticRepository(NamedParameterJdbcTemplate template) {
        this.namedParameterJdbcTemplate = template;
    }

    public long getUserCount(){
        String query = """
                SELECT COUNT(*) FROM "User";
                """;

        return namedParameterJdbcTemplate.queryForObject(query,new HashMap<>(),Long.class);
    }

    public long getShelterCount(){
        String query = """
                SELECT COUNT(*) FROM "User" WHERE role = 'SHELTER';
                """;

        return namedParameterJdbcTemplate.queryForObject(query,new HashMap<>(),Long.class);
    }

    public long getReportCount(){
        String query = """
                SELECT COUNT(*) FROM Report;
                """;

        return namedParameterJdbcTemplate.queryForObject(query,new HashMap<>(),Long.class);
    }

    public long getFoundReportCount(){
        String query = """
                SELECT COUNT(*) FROM REPORT WHERE report_status = 'FOUND';
                """;

        return namedParameterJdbcTemplate.queryForObject(query,new HashMap<>(),Long.class);
    }
}
