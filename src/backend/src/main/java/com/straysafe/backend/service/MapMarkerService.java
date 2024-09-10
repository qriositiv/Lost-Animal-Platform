package com.straysafe.backend.service;

import com.straysafe.backend.api.model.response.MapMarkerResponse;
import com.straysafe.backend.api.model.response.MapResponse;
import com.straysafe.backend.api.model.response.MapShelterResponse;
import com.straysafe.backend.domain.MapMarkerDAOResponse;
import com.straysafe.backend.domain.MapShelterDAOResponse;
import com.straysafe.backend.repository.MapMarkerRepository;
import com.straysafe.backend.repository.PetTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MapMarkerService {
    private final MapMarkerRepository mapMarkerRepository;
    private final PetTypeRepository petTypeRepository;

    public MapMarkerService(MapMarkerRepository mapMarkerRepository, PetTypeRepository petTypeRepository) {
        this.mapMarkerRepository = mapMarkerRepository;
        this.petTypeRepository = petTypeRepository;
    }

    public MapResponse getAllMarkers() {
        List<MapMarkerResponse> mapMarkers =
                convertMapMarkerDAOResponseIntoMapMarkerResponse(mapMarkerRepository.getMapMarker());
        List<MapShelterResponse> mapShelters =
                convertMapShelterDAOResponseIntoMapShelterResponse(mapMarkerRepository.getShelterMarker());


        return new MapResponse(
                mapMarkers,
                mapShelters
        );
    }

    private List<MapMarkerResponse> convertMapMarkerDAOResponseIntoMapMarkerResponse(List<MapMarkerDAOResponse> mapMarkerDAOResponses){
        return mapMarkerDAOResponses.stream()
                .map(mapMarkerResponse -> new MapMarkerResponse(
                        mapMarkerResponse.report_id(),
                        mapMarkerResponse.latitude(),
                        mapMarkerResponse.longitude(),
                        mapMarkerResponse.report_type(),
                        petTypeRepository.getTypeById(mapMarkerResponse.pet_type())
                )).toList();
    }

    private List<MapShelterResponse> convertMapShelterDAOResponseIntoMapShelterResponse(List<MapShelterDAOResponse> mapShelterDAOResponses){
        return mapShelterDAOResponses.stream()
                .map(mapShelterResponse -> new MapShelterResponse(
                        mapShelterResponse.shelter_id(),
                        mapShelterResponse.shelter_latitude(),
                        mapShelterResponse.shelter_longitude()
                )).toList();
    }

}
