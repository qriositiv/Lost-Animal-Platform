package com.straysafe.backend.domain;

import java.math.BigDecimal;

public record ImageComparisonDAORequest(
        BigDecimal latitude,
        BigDecimal longitude,
        String image,
        String petType
) {
}
