package com.straysafe.backend.service;

import com.fasterxml.uuid.Generators;
import com.straysafe.backend.api.model.exception.AuthenticationException;
import com.straysafe.backend.api.model.exception.BreedNotFoundException;
import com.straysafe.backend.api.model.exception.ImageSaveException;
import com.straysafe.backend.api.model.exception.PetCreateException;
import com.straysafe.backend.api.model.exception.PetDeleteException;
import com.straysafe.backend.api.model.exception.PetGetException;
import com.straysafe.backend.api.model.exception.PetTypeNotFoundException;
import com.straysafe.backend.api.model.request.PetRequest;
import com.straysafe.backend.api.model.response.PetListResponse;
import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.domain.PetDAORequest;
import com.straysafe.backend.domain.PetDAOResponse;
import com.straysafe.backend.repository.BreedRepository;
import com.straysafe.backend.repository.ImageRepository;
import com.straysafe.backend.repository.PetRepository;
import com.straysafe.backend.repository.PetTypeRepository;
import com.straysafe.backend.util.enums.PetType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
public class PetService {

    private static final String PET_DELETE_FAIL_MESSAGE = "Succesfully deleted pet %s.";
    private static final String PET_TYPE_NOT_FOUND_MESSAGE = "Pet type not found %s.";
    private static final String PET_BREED_NOT_FOUND_MESSAGE = "Breed '%s' not found for pet type '%s'.";
    private static final String FAILED_TO_CREATE_PET_RECORD_MESSAGE = "Failed to create pet record.";
    private static final String WRONG_IMAGE_FORMAT_MESSAGE = "The provided image is not a PNG image.";
    private static final String FAILED_TO_SAVE_IMAGE_MESSAGE = "Failed to save image.";
    private static final String FAILED_TO_RETRIEVE_PET_DATA_MESSAGE = "Pet: '%s' could not be retrieve.";
    private static final String UNAUTHORIZED_TO_DELETE_PET = "User unauthorized to delete pet: %s";

    private final PetRepository petRepository;
    private final BreedRepository breedRepository;
    private final PetTypeRepository petTypeRepository;
    private final ImageRepository imageRepository;

    private final ImageParserService imageParserService;

    public PetService(PetRepository petRepository, BreedRepository breedRepository, PetTypeRepository petTypeRepository,
                      ImageRepository imageRepository, ImageParserService imageParserService) {
        this.petRepository = petRepository;
        this.breedRepository = breedRepository;
        this.petTypeRepository = petTypeRepository;
        this.imageRepository = imageRepository;
        this.imageParserService = imageParserService;
    }

    @Transactional
    public String createPet(PetRequest petRequest) {

        UserCredentialResponse userData = (UserCredentialResponse) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        PetType petType;
        String petBreed;

        if (petRequest.petLostRequest() == null) {
            petType = petRequest.petSeenRequest().petType();
            petBreed = petRequest.petSeenRequest().breed();
        } else {
            petType = petRequest.petLostRequest().petType();
            petBreed = petRequest.petLostRequest().breed();
        }

        int petTypeId;
        try {
            petTypeId = petTypeRepository.getIdByType(petType);
        } catch (Exception e) {
            throw new PetTypeNotFoundException(PET_TYPE_NOT_FOUND_MESSAGE.formatted(petType));
        }

        int breedId;
        try {
            breedId = breedRepository.getIdByTypeAndName(petTypeId, petBreed);
        } catch (Exception e) {
            throw new BreedNotFoundException(PET_BREED_NOT_FOUND_MESSAGE.formatted(petBreed,petType));
        }

        PetDAORequest petDAORequest = convertPetRequestIntoPetDAORequest(petRequest, userData, petTypeId, breedId);

        String petId;
        try {
            petId = petRepository.createPet(petDAORequest);
        } catch (Exception e) {
            throw new PetCreateException(FAILED_TO_CREATE_PET_RECORD_MESSAGE + e);
        }

        byte[] imageBytes = Base64.getDecoder().decode(petRequest.image());
        try {
            if (!imageParserService.isPngImage(imageBytes)) {
                throw new IOException(WRONG_IMAGE_FORMAT_MESSAGE);
            } else {
                imageParserService.saveImage(imageBytes, petId);
            }
        } catch (Exception e) {
            throw new ImageSaveException(FAILED_TO_SAVE_IMAGE_MESSAGE + e.getMessage());
        }

        return petId;

    }

    public List<PetListResponse> getPetsByUserId(String username) {
        List<PetDAOResponse> petDAOResponses = petRepository.getUserPets(username);
        if (petDAOResponses != null) {
            return petDAOResponses.stream().map(petDAOResponse -> new PetListResponse(
                    petDAOResponse.petId(),
                    petDAOResponse.petName(),
                    petTypeRepository.getTypeById(petDAOResponse.petTypeId()),
                    breedRepository.getBreedById(petDAOResponse.breedId()),
                    petDAOResponse.petGender(),
                    petDAOResponse.petSize(),
                    petDAOResponse.petAge(),
                    imageParserService.convertPetImagePathToPetBase64(imageRepository.getImagePathById(petDAOResponse.petId())))).toList();
        } else {
            return Collections.emptyList();
        }
    }


        public String removePet(String petId) {
        UserCredentialResponse userData = (UserCredentialResponse) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        PetDAOResponse petDAOResponse;
        try {
            petDAOResponse = petRepository.getPetById(petId);
        } catch (Exception e) {
            throw new PetGetException(FAILED_TO_RETRIEVE_PET_DATA_MESSAGE.formatted(petId) + e.getMessage());
        }

        if (userData.getId().equals(petDAOResponse.userId())) {

            try {
                petRepository.deletePet(petId);
                return petId;
            } catch (Exception e) {
                throw new PetDeleteException(PET_DELETE_FAIL_MESSAGE.formatted(petId));
            }
        } else {
            throw new AuthenticationException(UNAUTHORIZED_TO_DELETE_PET.formatted(petId));
        }
    }

    private PetDAORequest convertPetRequestIntoPetDAORequest(PetRequest petRequest,
                                                             UserCredentialResponse userCredentialResponse, int petTypeId, int breedId) {
        String petUUID = Generators.timeBasedEpochGenerator().generate().toString();
        if (petRequest.petLostRequest() != null) {
            return new PetDAORequest(
                    petUUID,
                    userCredentialResponse.getId(),
                    petRequest.petLostRequest().petName(),
                    petTypeId,
                    breedId,
                    petRequest.petLostRequest().petGender().toString(),
                    petRequest.petLostRequest().petSize().toString(),
                    petRequest.petLostRequest().petAge().toString());
        } else {
            return new PetDAORequest(
                    petUUID,
                    userCredentialResponse.getId(),
                    null,
                    petTypeId,
                    breedId,
                    petRequest.petSeenRequest().petGender().toString(),
                    petRequest.petSeenRequest().petSize().toString(),
                    petRequest.petSeenRequest().petAge().toString());
        }
    }

}
