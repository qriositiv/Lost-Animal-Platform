package com.straysafe.backend.service;

import com.fasterxml.uuid.Generators;
import com.straysafe.backend.api.model.exception.ReportCreationException;
import com.straysafe.backend.api.model.exception.ReportNotFoundException;
import com.straysafe.backend.api.model.request.GridviewReportRequest;
import com.straysafe.backend.api.model.request.ReportRequest;
import com.straysafe.backend.api.model.response.ListReportResponse;
import com.straysafe.backend.api.model.response.PetResponse;
import com.straysafe.backend.api.model.response.ReportResponse;
import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.api.model.response.UserReportInfoResponse;
import com.straysafe.backend.domain.ListReportDAOResponse;
import com.straysafe.backend.domain.PetDAOResponse;
import com.straysafe.backend.domain.ReportDAORequest;
import com.straysafe.backend.domain.ReportDAOResponse;
import com.straysafe.backend.domain.UserDAOResponse;
import com.straysafe.backend.repository.BreedRepository;
import com.straysafe.backend.repository.ImageRepository;
import com.straysafe.backend.repository.PetRepository;
import com.straysafe.backend.repository.PetTypeRepository;
import com.straysafe.backend.repository.ReportRepository;
import com.straysafe.backend.repository.UserRepository;
import com.straysafe.backend.util.SqlQueryBuilder;
import com.straysafe.backend.util.enums.PetType;
import com.straysafe.backend.util.enums.ReportStatus;
import com.straysafe.backend.util.enums.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final PetTypeRepository petTypeRepository;
    private final BreedRepository breedRepository;
    private final PetRepository petRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    private final ImageParserService imageParserService;
    private final MailSenderService emailService;

    public ReportService(ReportRepository reportRepository,
                         PetTypeRepository petTypeRepository,
                         BreedRepository breedRepository,
                         PetRepository petRepository,
                         ImageRepository imageRepository,
                         UserRepository userRepository,
                         ImageParserService imageParserService,
                         MailSenderService emailService) {
        this.reportRepository = reportRepository;
        this.petTypeRepository = petTypeRepository;
        this.breedRepository = breedRepository;
        this.petRepository = petRepository;
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.imageParserService = imageParserService;
        this.emailService = emailService;
    }

    @Transactional
    public void createReport(ReportRequest reportRequest) {

        UserCredentialResponse userData = (UserCredentialResponse) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        ReportDAORequest reportDAORequest = convertReportRequestIntoReportDAORequest(
                userData,
                ReportStatus.ACTIVE,
                reportRequest);

        try {
            reportRepository.createReport(reportDAORequest);
            emailService.sendLostOrSeenEmail(reportDAORequest, userData);
        } catch (Exception e) {
            throw new ReportCreationException("Failed to create report " + e.getMessage());
        }

    }

    public List<ListReportResponse> getAllReports() {
        List<ReportDAOResponse> reportsDAOResponses;
        try {
            reportsDAOResponses = reportRepository.getAllReports();
        } catch (Exception e) {
            throw new ReportNotFoundException("Failed to get all reports " + e.getMessage());
        }

        List<PetDAOResponse> petsDAOResponses;
        try {
            petsDAOResponses = petRepository.getAllPets();
        } catch (Exception e) {
            throw new ReportNotFoundException("Failed to get pet data for reports " + e.getMessage());
        }

        return reportsDAOResponses.stream()
                .map(reportDAOResponse -> new ListReportResponse(
                        reportDAOResponse.userId(),
                        reportDAOResponse.reportId(),
                        getRequiredPetData(petsDAOResponses, reportDAOResponse.petId()),
                        reportDAOResponse.reportType(),
                        reportDAOResponse.address(),
                        reportDAOResponse.latitude(),
                        reportDAOResponse.longitude(),
                        reportDAOResponse.note(),
                        reportDAOResponse.createdAt()))
                .toList();
    }

    public List<ListReportResponse> getAllReportsByUserId(String userId) {
        List<ReportDAOResponse> reportsDAOResponses;
        try {
            reportsDAOResponses = reportRepository.getAllReportsByUserId(userId);
        } catch (Exception e) {
            throw new ReportNotFoundException("Failed to get all reports created by user" + userId + e.getMessage());
        }

        List<PetDAOResponse> petsDAOResponses;
        try {
            petsDAOResponses = petRepository.getAllPets();
        } catch (Exception e) {
            throw new ReportNotFoundException("Failed to get pet data for reports " + e.getMessage());
        }

        return reportsDAOResponses.stream()
                .map(reportDAOResponse -> new ListReportResponse(
                        reportDAOResponse.userId(),
                        reportDAOResponse.reportId(),
                        getRequiredPetData(petsDAOResponses, reportDAOResponse.petId()),
                        reportDAOResponse.reportType(),
                        reportDAOResponse.address(),
                        reportDAOResponse.latitude(),
                        reportDAOResponse.longitude(),
                        reportDAOResponse.note(),
                        reportDAOResponse.createdAt()))
                .toList();
    }

    public ReportResponse getReportById(String reportId) {
        try {
            ReportDAOResponse reportDAOResponse = reportRepository.getReportById(reportId);
            UserDAOResponse userDAOResponse = userRepository.getUserById(reportDAOResponse.userId());
            PetDAOResponse petDAOResponse = petRepository.getPetById(reportDAOResponse.petId());
            return new ReportResponse(
                    reportDAOResponse.reportId(),
                    convertUserDAOResponseIntoUserReportData(userDAOResponse, reportDAOResponse.userId()),
                    convertPetDAOResponseIntoPetResponse(petDAOResponse),
                    reportDAOResponse.reportType(),
                    reportDAOResponse.reportStatus(),
                    reportDAOResponse.address(),
                    reportDAOResponse.latitude(),
                    reportDAOResponse.longitude(),
                    reportDAOResponse.note(),
                    reportDAOResponse.createdAt());
        } catch (Exception e) {
            throw new ReportNotFoundException("Could not retrieve report :" + reportId + "\n" + e.getMessage());
        }
    }

    public void updateReport(String reportId) {
        UserCredentialResponse apiIssuerData = (UserCredentialResponse) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String reportOwnerId = reportRepository.getOwnerByReportId(reportId);

        if (!isOwner(reportOwnerId, apiIssuerData)) {
            throw new IllegalArgumentException("You are not allowed to update this report");
        } else {
            reportRepository.updateReportById(reportId);
        }

    }

    public void blockReport(String reportId) {
        UserCredentialResponse apiIssuerData = (UserCredentialResponse) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (!isOwner("", apiIssuerData)) {
            throw new IllegalArgumentException("You are not allowed to block this report");
        } else {
            reportRepository.blockReportById(reportId);
        }

    }

    public void deleteReport(String reportId) {
        UserCredentialResponse apiIssuerData = (UserCredentialResponse) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String reportOwnerId = reportRepository.getOwnerByReportId(reportId);

        if (!isOwner(reportOwnerId, apiIssuerData)) {
            throw new IllegalArgumentException("You are not allowed to delete this report");
        } else {
            reportRepository.deleteReportById(reportId);
        }

    }

    public List<ListReportResponse> getAllReportsFiltered(GridviewReportRequest request) {
        Map<String, Object> params = new HashMap<>();
        String query = SqlQueryBuilder.buildQuery(request, params);

        List<ListReportDAOResponse> reportsDAOResponses = reportRepository.getListReportWithFilters(query,params);
        if(reportsDAOResponses.isEmpty()){
            return Collections.emptyList();
        }
        return convertReportListDAOResponsesToResponse(reportsDAOResponses);
    }


    private UserReportInfoResponse convertUserDAOResponseIntoUserReportData(UserDAOResponse userDAOResponse, String userId) {
        return new UserReportInfoResponse(
                userId,
                userDAOResponse.username(),
                userDAOResponse.firstName(),
                userDAOResponse.lastName(),
                userDAOResponse.email(),
                userDAOResponse.phoneNumber()
        );
    }

    private PetResponse convertPetDAOResponseIntoPetResponse(PetDAOResponse petDAOResponse) {
        PetType petType = petTypeRepository.getTypeById(petDAOResponse.petTypeId());
        String breedName = breedRepository.getBreedById(petDAOResponse.breedId());
        String imagePath = imageRepository.getImagePathById(petDAOResponse.petId());

        return new PetResponse(
                petDAOResponse.petName(),
                petType,
                breedName,
                petDAOResponse.petGender(),
                petDAOResponse.petSize(),
                petDAOResponse.petAge(),
                imageParserService.convertPetImagePathToPetBase64(imagePath));
    }

    private ReportDAORequest convertReportRequestIntoReportDAORequest(
            UserCredentialResponse userData,
            ReportStatus reportStatus,
            ReportRequest reportRequest) {
        String reportUUID = Generators.timeBasedGenerator().generate().toString();

        return new ReportDAORequest(
                userData.getId(),
                reportUUID,
                reportRequest.petId(),
                reportRequest.reportType(),
                reportStatus,
                reportRequest.address(),
                reportRequest.latitude(),
                reportRequest.longitude(),
                reportRequest.note());
    }

    private PetResponse getRequiredPetData(List<PetDAOResponse> petDAOResponses, String petId) {
        try {
            PetDAOResponse requiredPetData = petDAOResponses.stream()
                    .filter(petDAOResponse -> petDAOResponse.petId().equals(petId))
                    .findFirst()
                    .orElseThrow();

            return new PetResponse(
                    requiredPetData.petName(),
                    petTypeRepository.getTypeById(requiredPetData.petTypeId()),
                    breedRepository.getBreedById(requiredPetData.breedId()),
                    requiredPetData.petGender(),
                    requiredPetData.petSize(),
                    requiredPetData.petAge(),
                    imageParserService.convertPetImagePathToPetBase64(imageRepository.getImagePathById(petId)));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private List<ListReportResponse> convertReportListDAOResponsesToResponse(List<ListReportDAOResponse> reportsDAOResponses){
       return reportsDAOResponses.stream().map(response -> {
           String image = imageParserService.convertPetImagePathToPetBase64(response.pet().petImage());

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
                            image
                    ),
                    response.reportType(),
                    response.address(),
                    response.latitude(),
                    response.longitude(),
                    response.note(),
                    response.createdAt()
            );
        }).toList();

    }

    private boolean isOwner(String reportOwnerId, UserCredentialResponse apiIssuerData) {
        if (reportOwnerId.equals(apiIssuerData.getId()) ||
                apiIssuerData.getRole().equals(Role.MODERATOR) ||
                apiIssuerData.getRole().equals(Role.SUPERUSER)) {
            return true;
        }
        return false;
    }


}