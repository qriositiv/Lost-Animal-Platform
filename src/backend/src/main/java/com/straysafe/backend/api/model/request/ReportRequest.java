package com.straysafe.backend.api.model.request;

import com.straysafe.backend.util.enums.ReportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReportRequest(
        @NotNull
        ReportType reportType,
        @NotNull(message = "Longitude is required")
        BigDecimal longitude,
        @NotNull(message = "Latitude is required")
        BigDecimal latitude,
        @NotBlank(message = "Address is required")
        String address,
        @NotBlank(message = "Pet is required")
        String petId,
        String note
) {
}
