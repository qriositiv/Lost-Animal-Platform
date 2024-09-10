package com.straysafe.backend.service;

import com.straysafe.backend.api.model.exception.AppException;
import com.straysafe.backend.api.model.request.UserCredentialRequest;
import com.straysafe.backend.api.model.request.UserRegisterRequest;
import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.domain.UserCredentialDAOResponse;
import com.straysafe.backend.repository.UserAuthRepository;
import com.straysafe.backend.util.templates.Templates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthService {

  UserAuthRepository userAuthRepository;
  PasswordEncoder passwordEncoder;
  MailSenderService emailService;

  @Autowired
  public UserAuthService(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, MailSenderService emailService) {
    this.userAuthRepository = userAuthRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public UserCredentialResponse login(UserCredentialRequest userCredentials) {
    UserCredentialDAOResponse userCredentialDAOResponse = userAuthRepository.getUserByLogin(userCredentials.login())
        .orElseThrow(() -> new AppException("Unknown user", HttpStatus.IM_USED));

    if (passwordEncoder.matches(userCredentials.password(), userCredentialDAOResponse.password())) {
      return convertUserCredentialDAOResponseToUserCredentialResponse(userCredentialDAOResponse);

    } else {
      throw new AppException("Unknown user", HttpStatus.IM_USED);
    }

  }

  public UserCredentialResponse register(UserRegisterRequest userCredentials) {
    try {
      UserCredentialResponse userCredentialResponse =  convertUserCredentialDAOResponseToUserCredentialResponse(userAuthRepository.registerUser(userCredentials));
      emailService.sendEmail(userCredentials.email(),
              Templates.REGISTER_WELCOME_SUBJECT.formatted(userCredentials.login()),
              Templates.REGISTER_WELCOME_EMAIL.formatted(userCredentials.firstName(),userCredentials.lastName()));

      return userCredentialResponse;
    } catch (Exception e) {
      throw new AppException("User already exists"+e.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }

  private UserCredentialResponse convertUserCredentialDAOResponseToUserCredentialResponse(
      UserCredentialDAOResponse userCredentialDAOResponse) {
    return new UserCredentialResponse(userCredentialDAOResponse.id(), userCredentialDAOResponse.firstName(),
        userCredentialDAOResponse.lastName(), userCredentialDAOResponse.login(),userCredentialDAOResponse.email(), userCredentialDAOResponse.phone(), userCredentialDAOResponse.role(),
        null);
  }

}
