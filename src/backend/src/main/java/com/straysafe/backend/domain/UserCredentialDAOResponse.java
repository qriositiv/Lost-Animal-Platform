package com.straysafe.backend.domain;

import com.straysafe.backend.util.enums.Role;

public record UserCredentialDAOResponse(
        String id,
        String firstName,
        String lastName,
        String login,
        String password,
        String email,
        String phone,
        Role role
) {
}
