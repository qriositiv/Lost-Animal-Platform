package com.straysafe.backend.api.model.request;

import java.math.BigDecimal;

public record GridviewLocationRequest(
    BigDecimal latitude,
    BigDecimal longitude,
    Integer distance
){}
