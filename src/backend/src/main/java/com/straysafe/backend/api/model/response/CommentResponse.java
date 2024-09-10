package com.straysafe.backend.api.model.response;

import com.straysafe.backend.util.enums.Role;

import java.time.LocalDateTime;

public record CommentResponse (
        String commentId,
        String reportId,
        String username,
        Role role,
        String comment,
        LocalDateTime createdAt
) {
}
