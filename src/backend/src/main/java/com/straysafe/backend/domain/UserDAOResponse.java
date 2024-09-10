package com.straysafe.backend.domain;

public record UserDAOResponse (
    String username,
    String firstName,
    String lastName,
    String email,
    String phoneNumber
    ){}
