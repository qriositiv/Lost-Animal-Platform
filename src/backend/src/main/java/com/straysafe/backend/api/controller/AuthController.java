package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.request.UserCredentialRequest;
import com.straysafe.backend.api.model.request.UserRegisterRequest;
import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.config.security.UserAuthProvider;
import com.straysafe.backend.service.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthService userAuthService;
    private final UserAuthProvider userAuthProvider;

    @Autowired
    public AuthController(UserAuthService userAuthService, UserAuthProvider userAuthProvider){
        this.userAuthService = userAuthService;
        this.userAuthProvider = userAuthProvider;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserCredentialResponse login(@Valid @RequestBody UserCredentialRequest userCredentials) {
        UserCredentialResponse user = userAuthService.login(userCredentials);
        user.setToken(userAuthProvider.createToken(user));
        return user;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCredentialResponse register(@Valid @RequestBody UserRegisterRequest userCredentials) {
        UserCredentialResponse user =  userAuthService.register(userCredentials);
        user.setToken(userAuthProvider.createToken(user));
        return user;
    }

}
