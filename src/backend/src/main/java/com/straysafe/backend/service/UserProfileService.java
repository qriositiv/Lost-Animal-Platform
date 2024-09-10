package com.straysafe.backend.service;

import com.straysafe.backend.api.model.exception.UserProfileException;
import com.straysafe.backend.api.model.request.ProfileSecurityUpdateRequest;
import com.straysafe.backend.api.model.request.ProfileUpdateRequest;
import com.straysafe.backend.api.model.response.ShelterProfileResponse;
import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.api.model.response.UserProfileResponse;
import com.straysafe.backend.domain.ProfileUpdateDAORequest;
import com.straysafe.backend.domain.SecurityUpdateDAORequest;
import com.straysafe.backend.domain.ShelterProfileDAOResponse;
import com.straysafe.backend.domain.UserProfileDAOResponse;
import com.straysafe.backend.repository.UserProfileRepository;
import com.straysafe.backend.util.enums.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
  private final UserProfileRepository userProfileRepository;
  private final PasswordEncoder passwordEncoder;

  public UserProfileService(UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
    this.userProfileRepository = userProfileRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserProfileResponse getProfileByUsername(String username) {
    try {
      UserProfileDAOResponse response = userProfileRepository.getUserProfileById(username);
      return new UserProfileResponse(
          response.userId(),
          response.firstName(),
          response.lastName(),
          response.username(),
          response.email(),
          response.telephone(),
          response.role(),
          response.totalUserReportCount(),
          response.totalUserCommentCount(),
          response.createdAt());
    } catch (Exception e) {
      throw new UserProfileException("User profile not found");
    }

  }

  public void updateProfile(ProfileUpdateRequest profileUpdateRequest) {
    UserCredentialResponse apiIssuerData = (UserCredentialResponse) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
    userProfileRepository.updateProfile(convertProfileUpdateRequestToDAO(profileUpdateRequest,apiIssuerData));
  }

  public void updateSecurityAndOther(ProfileSecurityUpdateRequest profileSecurityUpdateRequest) {
    UserCredentialResponse apiIssuerData = (UserCredentialResponse) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
    userProfileRepository.updateSecurityAndOther(convertSecurityUpdateRequestToDAO(profileSecurityUpdateRequest, apiIssuerData));
  }

  public void blockUser(String userId) {
    UserCredentialResponse apiIssuerData = (UserCredentialResponse) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();

    if (!isOwner("", apiIssuerData)) {
      throw new IllegalArgumentException("You are not allowed to block this user");
    } else {
      userProfileRepository.blockUser(userId);
    }
  }

  public ShelterProfileResponse getShelterById(String shelterId) {
    try {
      ShelterProfileDAOResponse response = userProfileRepository.getShelterById(shelterId);
      return new ShelterProfileResponse(
              response.shelterId(),
              response.shelterName(),
              response.shelterAddress(),
              response.shelterLatitude(),
              response.shelterLongitude(),
              response.userId(),
              response.username(),
              response.email(),
              response.telephone(),
              response.role(),
              response.createdAt());
    } catch (Exception e) {
      throw new UserProfileException("Shelter profile not found");
    }

  }

  private ProfileUpdateDAORequest convertProfileUpdateRequestToDAO(ProfileUpdateRequest profileUpdateRequest, UserCredentialResponse apiIssuerData){
    return new ProfileUpdateDAORequest(
      apiIssuerData.getId(),
      profileUpdateRequest.image(),
      profileUpdateRequest.email(),
      profileUpdateRequest.firstName(),
      profileUpdateRequest.lastName(),
      profileUpdateRequest.username(),
      profileUpdateRequest.telephone());
  }

  private SecurityUpdateDAORequest convertSecurityUpdateRequestToDAO(ProfileSecurityUpdateRequest profileSecurityUpdateRequest, UserCredentialResponse apiIssuerData){
    return new SecurityUpdateDAORequest(
            apiIssuerData.getId(),
            passwordEncoder.encode(profileSecurityUpdateRequest.password()));
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
