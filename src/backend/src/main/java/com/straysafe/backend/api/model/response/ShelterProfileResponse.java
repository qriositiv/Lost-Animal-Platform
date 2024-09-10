package com.straysafe.backend.api.model.response;

import com.straysafe.backend.util.enums.Role;

import java.time.LocalDateTime;

public record ShelterProfileResponse(
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
