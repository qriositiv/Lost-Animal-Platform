package com.straysafe.backend.api.model.response;

import com.straysafe.backend.util.enums.PetAge;
import com.straysafe.backend.util.enums.PetGender;
import com.straysafe.backend.util.enums.PetSize;
import com.straysafe.backend.util.enums.PetType;

public record PetResponse (
    String petName,
    PetType petType,
    String petBreed,
    PetGender petGender,
    PetSize petSize,
    PetAge petAge,
    String petImage
) {}
