import { Component, OnInit, inject } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { UserPetsComponent } from '../user-pets/user-pets.component';
import { PublicService } from '../../../../services/public.service';
import { UserProfileResponse } from '../../../../interfaces/types';
import { AuthService } from '../../../../services/auth.service';
import { formatDate } from '@angular/common';
import { FormatDateArrayPipe, FormatShortDatePipe } from '../../../../pipes/format-date.pipe';
import { faBan } from '@fortawesome/free-solid-svg-icons';
import { UserService } from '../../../../services/user.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [FaIconComponent, UserPetsComponent, FormatDateArrayPipe, FormatShortDatePipe],
  templateUrl: './user-profile.component.html',
})
export class UserProfileComponent implements OnInit {
  authService = inject(AuthService);
  publicService = inject(PublicService);
  userService = inject(UserService);
  route = inject(ActivatedRoute);
  user!: string;
  guestRole!: string;
  faBlock = faBan;
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
  userCreatedAt = [2024, 5, 29, 20, 56, 39, 629932000];

  constructor(
    private toastr: ToastrService,
    private router: Router,
  ) {}

  ngOnInit() {
    const token = this.authService.decodeToken();
    if (token) {
      this.guestRole = token.role;
    }

    this.route.paramMap.subscribe((params) => {
      const username = params.get('username') || '';
      this.publicService.getProfileByUsername(username).subscribe({
        next: (response: UserProfileResponse) => {
          this.userProfile = response;
        },
        error: (error) => {
          this.toastr.error('Failed to get user data', 'Error');
          console.error(error);
          this.router.navigate(['page-404']);
        },
      });
    });
  }

  onBlockUser(userId: string): void {
    this.userService.blockUser(userId).subscribe({
      next: () => {
        this.toastr.success(`User with ID ${userId} has been blocked successfully`, 'Success!');
        this.router.navigate(['']);
      },
      error: (error) => {
        console.error(error, 'Error');
      },
    });
  }

  openUserReports(username: string) {
    this.router.navigate([username, 'reports']);
  }
}
