package com.straysafe.backend.domain;

import com.straysafe.backend.util.enums.ReportType;

import java.math.BigDecimal;

public record MapMarkerDAOResponse (

    String report_id,
    BigDecimal latitude,
    BigDecimal longitude,
    ReportType report_type,
    int pet_type
){}

