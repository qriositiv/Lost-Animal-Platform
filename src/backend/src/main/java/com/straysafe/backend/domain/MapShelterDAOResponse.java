package com.straysafe.backend.domain;

import java.math.BigDecimal;

public record MapShelterDAOResponse (
        String shelter_id,
        BigDecimal shelter_latitude,
        BigDecimal shelter_longitude
){ }
