package com.straysafe.backend.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCredentialRequest(

        @Size(min = 6, max = 20, message = "Login must contain between 6 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login must contain only letters and numbers")
        String login,
        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?!.*\\s)(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
        String password) {
}