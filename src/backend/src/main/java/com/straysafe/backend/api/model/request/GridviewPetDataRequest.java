package com.straysafe.backend.api.model.request;

import com.straysafe.backend.util.enums.PetGender;
import com.straysafe.backend.util.enums.PetType;

import java.util.List;

public record GridviewPetDataRequest(
        List<PetType> petTypeList,
        List<String> petBreedList,
        List<PetGender> petGenderList) {
}
