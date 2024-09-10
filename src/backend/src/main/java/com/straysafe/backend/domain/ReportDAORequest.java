package com.straysafe.backend.domain;

import com.straysafe.backend.util.enums.ReportStatus;
import com.straysafe.backend.util.enums.ReportType;

import java.math.BigDecimal;

public record ReportDAORequest(
        String userId,
        String reportId,
        String petId,
        ReportType reportType,
        ReportStatus reportStatus,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String note
) {
}
