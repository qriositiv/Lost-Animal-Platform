package com.straysafe.backend.domain;

import com.straysafe.backend.util.enums.Role;

import java.time.LocalDateTime;

public record CommentDAOResponse(
        String commentId,
        String reportId,
        String userId,
        String username,
        Role role,
        String comment,
        LocalDateTime updatedAt,
        LocalDateTime createdAt
) {
}
