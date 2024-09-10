package com.straysafe.backend.api.model.response;

import com.straysafe.backend.util.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredentialResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String login;
    private String email;
    private String phone;
    private Role role;
    private String token;
}
