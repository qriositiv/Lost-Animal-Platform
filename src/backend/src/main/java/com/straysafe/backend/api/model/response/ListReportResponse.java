package com.straysafe.backend.api.model.response;

import com.straysafe.backend.util.enums.ReportType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListReportResponse(
        String userId,
        String reportId,
        PetResponse pet,
        ReportType reportType,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String note,
        LocalDateTime createdAt) {
}
