package com.straysafe.backend.domain;

import com.straysafe.backend.util.enums.Role;

import java.time.LocalDateTime;

public record ShelterProfileDAOResponse(
        String shelterId,
        String shelterName,
        String shelterAddress,
        String shelterLatitude,
        String shelterLongitude,
        String userId,
        String username,
        String email,
        String telephone,
        Role role,
        LocalDateTime createdAt
) {
}
