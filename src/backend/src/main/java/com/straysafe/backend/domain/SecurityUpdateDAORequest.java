package com.straysafe.backend.domain;

public record SecurityUpdateDAORequest(
        String userId,
        String password
) {
}
