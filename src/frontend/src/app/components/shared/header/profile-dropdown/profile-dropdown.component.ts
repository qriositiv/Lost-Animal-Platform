import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { Router, RouterLink } from '@angular/router';
import { clickedOutside } from '../../../../directives/clickedOutside.directive';
import { AuthService } from '../../../../services/auth.service';
import { PublicService } from '../../../../services/public.service';
import { UserProfileResponse } from '../../../../interfaces/types';

@Component({
  selector: 'app-profile-dropdown',
  standalone: true,
  imports: [CommonModule, clickedOutside, FontAwesomeModule, RouterLink],
  templateUrl: './profile-dropdown.component.html',
})
export class ProfileDropdownComponent {
  dropdownOpen: boolean = false;
  faUser = faUser;
  username!: string;
  authService = inject(AuthService);
  publicService = inject(PublicService);
  router = inject(Router);

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onClickedOutside(): void {
    this.dropdownOpen = false;
  }

  signOut(): void {
    this.authService.logout();
  }

  sendToProfile(): void {
    const decToken = this.authService.decodeToken();
    const reportLink = decToken ? decToken.username + '/profile' : '#';
    this.router.navigate([reportLink]);
  }

  sendToReports(): void {
    const decToken = this.authService.decodeToken();
    const reportLink = decToken ? decToken.username + '/reports' : '#';
    this.router.navigate([reportLink]);
  }

  ngOnInit() {
    const token = this.authService.decodeToken();
    if (!token) return;
    this.username = token.username;
  }
}
