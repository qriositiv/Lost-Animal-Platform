package com.straysafe.backend.config.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.straysafe.backend.api.model.exception.AppException;
import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.domain.UserCredentialDAOResponse;
import com.straysafe.backend.repository.UserAuthRepository;
import com.straysafe.backend.util.enums.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class UserAuthProvider {

    @Value("${security.jwt.token.secret-key:secret-key}")
    private String secretKey;

    private final UserAuthRepository userAuthRepository;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserCredentialResponse user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000);

        return JWT.create()
                .withSubject(user.getLogin())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("id", user.getId())
                .withClaim("role", user.getRole().name())
                .withClaim("firstName", user.getFirstName())
                .withClaim("lastName", user.getLastName())
                .withClaim("username", user.getLogin())
                .sign(Algorithm.HMAC256(secretKey));
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();


        DecodedJWT decodedJWT = verifier.verify(token);

        UserCredentialResponse user = UserCredentialResponse.builder()
                .login(decodedJWT.getSubject())
                .id(decodedJWT.getClaim("id").asString())
                .firstName(decodedJWT.getClaim("firstName").asString())
                .lastName(decodedJWT.getClaim("lastName").asString())
                .login(decodedJWT.getClaim("username").asString())
                .role(Role.valueOf(decodedJWT.getClaim("role").asString()))
                .build();

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        JWTVerifier verifier = JWT.require(algorithm).build();

        DecodedJWT decodedJWT = verifier.verify(token);
        UserCredentialDAOResponse user = userAuthRepository.getUserByLogin(decodedJWT.getClaim("username").asString())
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        return new UsernamePasswordAuthenticationToken(
                convertUserCredentialDAOResponseToUserCredentialResponse(user),
                null,
                Collections.emptyList());
    }

    private UserCredentialResponse convertUserCredentialDAOResponseToUserCredentialResponse(UserCredentialDAOResponse userCredentialDAOResponse) {
        return new UserCredentialResponse(userCredentialDAOResponse.id(), userCredentialDAOResponse.firstName(), userCredentialDAOResponse.lastName(), userCredentialDAOResponse.login(), userCredentialDAOResponse.email(), userCredentialDAOResponse.phone(), userCredentialDAOResponse.role(), null);
    }

}
