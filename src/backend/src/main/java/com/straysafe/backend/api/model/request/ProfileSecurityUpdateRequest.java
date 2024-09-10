package com.straysafe.backend.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ProfileSecurityUpdateRequest(
        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?!.*\\s)(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
        String password
) {
}
