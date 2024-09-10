package com.straysafe.backend.api.model.response;

public record ImageComparisonResponse (
    float similarity,
    ListReportResponse report){
}
