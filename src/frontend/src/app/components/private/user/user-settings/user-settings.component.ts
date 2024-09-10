import { ChangeDetectorRef, Component, inject } from '@angular/core';
import { UserPetsComponent } from '../user-pets/user-pets.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faShield, faUser } from '@fortawesome/free-solid-svg-icons';
import { CommonModule } from '@angular/common';
import { ProfileSettingsComponent } from './profile-settings/profile-settings.component';
import { UserSettingsOtherComponent } from './other/user-settings-other.component';
import { UserProfileResponse } from '../../../../interfaces/types';
import { AuthService } from '../../../../services/auth.service';
import { PublicService } from '../../../../services/public.service';
// import { ToastNoAnimationModule, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-user-settings',
  standalone: true,
  imports: [ProfileSettingsComponent, UserPetsComponent, UserSettingsOtherComponent, FontAwesomeModule, CommonModule],
  templateUrl: './user-settings.component.html',
})
export class UserSettingsComponent {
  section: number = 0;
  authService = inject(AuthService);
  publicService = inject(PublicService);
  changeSection(sec: number): void {
    this.section = sec;
  }

  sections = [
    { index: 0, label: 'Profile', icon: faUser },
    { index: 1, label: 'Security & Other', icon: faShield },
  ];

  updateProfile(): void {
    // this.toastr.warning('Warning', 'Warning');
  }

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

  ngOnInit() {
    const token = this.authService.decodeToken();
    if (!token) return;

    const username = token.username;
    this.publicService.getProfileByUsername(username).subscribe({
      next: (response: UserProfileResponse) => {
        this.userProfile = response;
      },
      error: (error) => {
        console.error('ERROR FETCHING PROFILE DATA:', error);
      },
    });
  }
}
