package com.straysafe.backend.api.model.response;

import java.util.List;

public record MapResponse(
        List<MapMarkerResponse> mapMarkerResponse,
        List<MapShelterResponse> mapShelterResponse
) {

}
