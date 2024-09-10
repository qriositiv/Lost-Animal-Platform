package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.response.MapResponse;
import com.straysafe.backend.service.MapMarkerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/public/marker")
public class MapMarkersController {
    private final MapMarkerService mapMarkerService;

    public MapMarkersController(MapMarkerService mapMarkerService) {
        this.mapMarkerService = mapMarkerService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MapResponse getReportMarkers() {
        return mapMarkerService.getAllMarkers();
    }


}
