package com.straysafe.backend.domain;

public record CommentDAORequest(
        String userId,
        String reportId,
        String commentId,
        String comment
) {

}
