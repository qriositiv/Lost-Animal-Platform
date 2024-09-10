package com.straysafe.backend.api.model.response;

import com.straysafe.backend.util.enums.ReportStatus;
import com.straysafe.backend.util.enums.ReportType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportResponse(
        String reportId,
        UserReportInfoResponse user,
        PetResponse pet,
        ReportType reportType,
        ReportStatus reportStatus,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        String note,
        LocalDateTime createdAt
) {
}
