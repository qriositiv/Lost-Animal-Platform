package com.straysafe.backend.domain;

import com.straysafe.backend.util.enums.PetAge;
import com.straysafe.backend.util.enums.PetGender;
import com.straysafe.backend.util.enums.PetSize;

import java.time.LocalDateTime;

public record PetDAOResponse(
    String petId,
    String userId,
    String petName,
    int petTypeId,
    int breedId,
    PetGender petGender,
    PetSize petSize,
    PetAge petAge,
    LocalDateTime createdAt
    ) {}
