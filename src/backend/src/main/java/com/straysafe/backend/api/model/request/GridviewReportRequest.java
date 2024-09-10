package com.straysafe.backend.api.model.request;

import com.straysafe.backend.util.enums.ReportType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GridviewReportRequest (
    @NotNull(message = "Page number is required")
    @Min(value = 1, message = "Page number must be greater than 0")
    Long page,
    List<ReportType> reportTypeList,
    GridviewLocationRequest reportLocationRequest,
    Integer reportPublishHourRange,
    GridviewPetDataRequest petDataRequest,
    String reportCreator
){}
