package com.straysafe.backend.api.model.response;

import java.math.BigDecimal;

public record MapShelterResponse (
    String shelter_id,
    BigDecimal latitude,
    BigDecimal longitude
        ){}
