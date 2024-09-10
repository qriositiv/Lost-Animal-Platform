package com.straysafe.backend.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(

        @NotNull(message = "Report id is null")
        String reportId,
        @NotBlank(message = "Comment is required")
        String comment
) {
}
