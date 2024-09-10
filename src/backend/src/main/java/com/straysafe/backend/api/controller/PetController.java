package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.request.PetRequest;
import com.straysafe.backend.api.model.response.PetListResponse;
import com.straysafe.backend.service.PetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/pet/create")
    public String createPet(@Valid @RequestBody PetRequest petRequest) {
        return petService.createPet(petRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/public/pet/{username}")
    public List<PetListResponse> getPetsByUserId(@PathVariable("username") String username) {
        return petService.getPetsByUserId(username);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/pet/delete/{petId}")
    public String deletePet(@PathVariable("petId") String petId) {
        return petService.removePet(petId);
    }
}
