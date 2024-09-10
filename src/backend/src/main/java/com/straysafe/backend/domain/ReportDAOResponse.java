package com.straysafe.backend.domain;

import com.straysafe.backend.util.enums.ReportStatus;
import com.straysafe.backend.util.enums.ReportType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportDAOResponse(
        String reportId,
        String userId,
        String petId,
        ReportType reportType,
        ReportStatus reportStatus,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String note,
        LocalDateTime createdAt
) {
}
