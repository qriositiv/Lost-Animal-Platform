import { Component, OnInit, inject } from '@angular/core';
import { ProfileUpdateRequest, UserProfileResponse } from '../../../../../interfaces/types';
import { AuthService } from '../../../../../services/auth.service';
import { PublicService } from '../../../../../services/public.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserService } from '../../../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile-settings',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile-settings.component.html',
})
export class ProfileSettingsComponent implements OnInit {
  section: number = 0;
  authService = inject(AuthService);
  publicService = inject(PublicService);
  userProfileForm!: FormGroup;

  userProfile: UserProfileResponse = {
    userId: '',
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    telephone: '',
    role: '',
    totalUserReportCount: 0,
    totalUserCommentCount: 0,
    createdAt: '',
  };

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private toastr: ToastrService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    const token = this.authService.decodeToken();
    if (!token) return;

    const username = token.username;
    this.publicService.getProfileByUsername(username).subscribe({
      next: (response: UserProfileResponse) => {
        this.userProfile = response;
        this.userProfileForm.patchValue(response);
      },
      error: (error) => {
        console.error('ERROR FETCHING PROFILE DATA:', error);
      },
    });

    this.userProfileForm = this.fb.group({
      username: [
        '',
        [Validators.required, Validators.pattern('^[a-zA-Z0-9]+$'), Validators.minLength(6), Validators.maxLength(20)],
      ],
      email: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$')]],
      firstName: ['', [Validators.required, Validators.maxLength(60), Validators.pattern('^[a-zA-Z]+$')]],
      lastName: ['', [Validators.required, Validators.maxLength(60), Validators.pattern('^[a-zA-Z]+$')]],
      telephone: ['', [Validators.required, Validators.pattern('^\\+([0-9]{7,14})$')]],
    });
  }

  get username() {
    return this.userProfileForm.get('username');
  }

  get email() {
    return this.userProfileForm.get('email');
  }

  get firstName() {
    return this.userProfileForm.get('firstName');
  }

  get lastName() {
    return this.userProfileForm.get('lastName');
  }

  get telephone() {
    return this.userProfileForm.get('telephone');
  }

  changeSection(sec: number): void {
    this.section = sec;
  }

  onUpdateProfile(): void {
    if (this.userProfileForm.valid) {
      const profileUpdateRequest: ProfileUpdateRequest = this.userProfileForm.value;
      this.userService.updateProfile(profileUpdateRequest).subscribe({
        next: () => {
          this.toastr.success('Profile updated successfully', 'Success');
          this.router.navigate([`${profileUpdateRequest.username}/profile`]);
        },
        error: (error) => {
          this.toastr.error(error, 'Error updating profile');
        },
      });
    }
  }
}
