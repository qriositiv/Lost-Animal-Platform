package com.straysafe.backend.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message = "First name is required")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only letters")
        String firstName,
        @NotBlank(message = "Last name is required")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only letters")
        String lastName,
        @Size(min = 6, max = 20, message = "Login must contain between 6 and 20 characters")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login must contain only letters and numbers")
        String login,
        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?!.*\\s)(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
        String password,
        @NotBlank(message = "Email is required")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must be valid")
        String email,
        @NotBlank(message = "Phone is required")
        @Pattern(regexp = "^\\+([0-9]{7,14})$", message = "Phone must contain only numbers")
        String phone
) {
}
