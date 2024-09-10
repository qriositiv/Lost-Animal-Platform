package com.straysafe.backend.domain;

public record PetDAORequest(
    String petId,
    String userId,
    String petName,
    int petTypeId,
    int breedId,
    String petGender,
    String petSize,
    String petAge
){}
