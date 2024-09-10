package com.straysafe.backend.service;

import com.straysafe.backend.api.model.request.ImageComparisonRequest;
import com.straysafe.backend.api.model.response.ImageComparisonResponse;
import com.straysafe.backend.api.model.response.ListReportResponse;
import com.straysafe.backend.api.model.response.PetResponse;
import com.straysafe.backend.domain.ImageComparisonDAORequest;
import com.straysafe.backend.domain.ImageComparisonDAOResponse;
import com.straysafe.backend.domain.ListReportDAOResponse;
import com.straysafe.backend.domain.SimilarityReportDAORequest;
import com.straysafe.backend.domain.SimilarityReportDAOResponse;
import com.straysafe.backend.repository.ImageComparisonRepository;
import com.straysafe.backend.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ImageCompareService {

    private static final String WRONG_IMAGE_FORMAT_MESSAGE = "The provided image is not a PNG image.";

    private final ReportRepository reportRepository;
    private final ImageComparisonRepository imageComparisonRepository;
    private final ImageParserService imageParserService;


    public ImageCompareService(ImageComparisonRepository imageComparisonRepository, ImageParserService imageParserService, ReportRepository reportRepository) {
        this.imageComparisonRepository = imageComparisonRepository;
        this.imageParserService = imageParserService;
        this.reportRepository = reportRepository;
    }

    public List<ImageComparisonResponse> compareImages(ImageComparisonRequest imageComparisonRequest) {
        byte[] imageBytes = Base64.getDecoder().decode(imageComparisonRequest.image());
        try {
            if (!imageParserService.isPngImage(imageBytes)) {
                throw new IOException(WRONG_IMAGE_FORMAT_MESSAGE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        List<ImageComparisonDAOResponse> imageComparisonDAOResponse = imageComparisonRepository.getRequiredReportIds(
                convertImageComparisonRequestToDAO(imageComparisonRequest));

        if (imageComparisonDAOResponse.isEmpty()) {
            return Collections.emptyList();
        }

        List<SimilarityReportDAOResponse> similarityReportDAOResponse = reportRepository.getSimilarReportList(
                new SimilarityReportDAORequest(
                        imageComparisonRequest.image(),
                        imageComparisonDAOResponse));

        Collections.sort(similarityReportDAOResponse, Comparator.comparing(SimilarityReportDAOResponse::similarity));

        if (similarityReportDAOResponse.size() >= 8) {
            similarityReportDAOResponse = similarityReportDAOResponse.size() > 8
                    ? similarityReportDAOResponse.subList(0, 8)
                    : similarityReportDAOResponse;
        }

        return similarityReportDAOResponse.stream().map(
                        response -> new ImageComparisonResponse(
                                response.similarity(),
                                processListReportDAOResponse(
                                        reportRepository.getReportByIdImageComparison(response.reportId()))))
                .toList();

    }

    private ImageComparisonDAORequest convertImageComparisonRequestToDAO(ImageComparisonRequest imageComparisonRequest) {
        return new ImageComparisonDAORequest(
                imageComparisonRequest.latitude(),
                imageComparisonRequest.longitude(),
                imageComparisonRequest.image(),
                imageComparisonRequest.petType().toString()
        );
    }

    private ListReportResponse processListReportDAOResponse(ListReportDAOResponse response) {
        return new ListReportResponse(
                response.userId(),
                response.reportId(),
                new PetResponse(
                        response.pet().petName(),
                        response.pet().petType(),
                        response.pet().petBreed(),
                        response.pet().petGender(),
                        response.pet().petSize(),
                        response.pet().petAge(),
                        imageParserService.convertPetImagePathToPetBase64(response.pet().petImage())
                ),
                response.reportType(),
                response.address(),
                response.latitude(),
                response.longitude(),
                response.note(),
                response.createdAt()
        );
    }
}
