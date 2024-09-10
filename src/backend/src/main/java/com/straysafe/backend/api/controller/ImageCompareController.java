package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.request.ImageComparisonRequest;
import com.straysafe.backend.api.model.response.ImageComparisonResponse;
import com.straysafe.backend.service.ImageCompareService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
public class ImageCompareController {

    private final ImageCompareService imageCompareService;

    @Autowired
    public ImageCompareController(ImageCompareService imageCompareService) {
        this.imageCompareService = imageCompareService;
    }

    @PostMapping("/image/comparison")
    @ResponseStatus(HttpStatus.OK)
    public List<ImageComparisonResponse> getMostSimilarReports(@Valid @RequestBody ImageComparisonRequest imageComparisonRequest) {
        return imageCompareService.compareImages(imageComparisonRequest);
    }
}
