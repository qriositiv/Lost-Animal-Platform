package com.straysafe.backend.domain;

import java.util.List;

public record GridviewPetDataDAORequest (
    List<String> petTypeList,
    List<String> petBreedList,
    List<String> petGenderList
){}
