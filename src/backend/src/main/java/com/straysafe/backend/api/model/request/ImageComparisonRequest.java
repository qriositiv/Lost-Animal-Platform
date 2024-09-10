package com.straysafe.backend.api.model.request;

import com.straysafe.backend.util.enums.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ImageComparisonRequest(
        @NotNull(message = "Latitude is required")
        BigDecimal latitude,
        @NotNull(message = "Longitude is required")
        BigDecimal longitude,
        @NotBlank(message = "Image is required")
        String image,
        @NotNull(message = "Pet type is required")
        PetType petType
) {
}
