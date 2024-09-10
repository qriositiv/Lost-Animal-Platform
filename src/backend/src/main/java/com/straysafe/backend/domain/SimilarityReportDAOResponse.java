package com.straysafe.backend.domain;

public record SimilarityReportDAOResponse(
        String reportId,
        float similarity
) {
}
