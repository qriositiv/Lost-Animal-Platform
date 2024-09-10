package com.straysafe.backend.domain;

import java.util.List;

public record SimilarityReportDAORequest(
        String image,
        List<ImageComparisonDAOResponse> compare
){
}
