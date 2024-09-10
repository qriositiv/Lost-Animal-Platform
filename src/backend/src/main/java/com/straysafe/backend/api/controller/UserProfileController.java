
package com.straysafe.backend.api.controller;

import com.straysafe.backend.api.model.request.ProfileSecurityUpdateRequest;
import com.straysafe.backend.api.model.request.ProfileUpdateRequest;
import com.straysafe.backend.api.model.response.ShelterProfileResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.straysafe.backend.api.model.response.UserProfileResponse;
import com.straysafe.backend.service.UserProfileService;

@RestController
public class UserProfileController {

  private final UserProfileService userProfileService;

  public UserProfileController(UserProfileService userProfileService) {
    this.userProfileService = userProfileService;
  }

  @GetMapping("/public/users/{username}/profile")
  @ResponseStatus(HttpStatus.OK)
  public UserProfileResponse userProfile(
      @PathVariable("username") @NotBlank(message = "Username is required") String username) {
    return userProfileService.getProfileByUsername(username);
  }

  @PutMapping("/api/users/profile/update")
  @ResponseStatus(HttpStatus.OK)
  public void updateProfile(@RequestBody @Valid ProfileUpdateRequest profileUpdateRequest){
    userProfileService.updateProfile(profileUpdateRequest);
  }

  @PutMapping("/api/users/security/update")
  @ResponseStatus(HttpStatus.OK)
  public void updateSecurityAndOther(@RequestBody @Valid ProfileSecurityUpdateRequest profileSecurityUpdateRequest){
    userProfileService.updateSecurityAndOther(profileSecurityUpdateRequest);
  }

  @PutMapping("/api/users/security/block")
  @ResponseStatus(HttpStatus.OK)
  public void blockUser(@RequestBody @Valid String userId){
    userProfileService.blockUser(userId);
  }

  @GetMapping("/public/shelters/{ShelterId}/profile")
  @ResponseStatus(HttpStatus.OK)
  public ShelterProfileResponse shelterProfile(
          @PathVariable("ShelterId") @NotBlank(message = "ShelterId is required") String ShelterId) {
    return userProfileService.getShelterById(ShelterId);
  }
}
