package com.straysafe.backend.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProfileUpdateDAORequest(
        String userId,
        String image,
        String email,
        String firstName,
        String lastName,
        String username,
        String telephone
) {
}
