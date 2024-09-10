import { Component, OnInit, inject, resolveForwardRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ContentComponent } from '../../../public/features/search/search-reports/content/content.component';
import { Location } from '@angular/common';
import { PublicService } from '../../../../services/public.service';
import { UserProfileResponse } from '../../../../interfaces/types';
import { ToastrService } from 'ngx-toastr';
import { FormatDateArrayPipe, FormatShortDatePipe } from '../../../../pipes/format-date.pipe';

@Component({
  selector: 'app-user-reports',
  standalone: true,
  imports: [ContentComponent, FormatDateArrayPipe, FormatShortDatePipe],
  templateUrl: './user-reports.component.html',
  styleUrl: './user-reports.component.css',
})
export class UserReportsComponent implements OnInit {
  route = inject(ActivatedRoute);
  publicService = inject(PublicService);
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
  userCreatedAt!: string;
  author!: string;

  constructor(private location: Location, private toastr: ToastrService, private router: Router) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const username = params.get('username') || '';
      this.publicService.getProfileByUsername(username).subscribe({
        next: (response: UserProfileResponse) => {
          this.userProfile = response;
          this.author = response.userId;
        },
        error: (error) => {
          this.toastr.error('Failed to get user data', 'Error');
          this.router.navigate(['page-404']);
        },
      });
    });
  }

  openAuthorProfile(username: string) {
    this.router.navigate([username, 'profile']);
  }

  goBack(): void {
    this.location.back();
  }
}
