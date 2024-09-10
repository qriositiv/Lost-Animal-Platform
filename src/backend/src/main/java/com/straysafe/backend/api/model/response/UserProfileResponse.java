package com.straysafe.backend.api.model.response;

import java.time.LocalDateTime;
import com.straysafe.backend.util.enums.Role;

public record UserProfileResponse(
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
