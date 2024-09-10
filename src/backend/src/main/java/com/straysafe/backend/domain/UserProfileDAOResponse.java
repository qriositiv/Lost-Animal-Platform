package com.straysafe.backend.domain;

import java.time.LocalDateTime;
import com.straysafe.backend.util.enums.Role;

public record UserProfileDAOResponse(
    String userId,
    String firstName,
    String lastName,
    String username,
    String email,
    String telephone,
    Role role,
    long totalUserReportCount,
    long totalUserCommentCount,
    LocalDateTime createdAt) {
}
