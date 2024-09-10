package com.straysafe.backend.repository;

import com.straysafe.backend.api.model.request.UserRegisterRequest;
import com.straysafe.backend.domain.UserCredentialDAOResponse;

import java.util.Optional;

public interface UserAuthRepositoryInterface {
    Optional<UserCredentialDAOResponse> getUserByLogin(String login);

    UserCredentialDAOResponse registerUser(UserRegisterRequest userRegisterRequest);
}
