import { Component } from '@angular/core';
import { ContentComponent } from '../../../public/features/search/search-reports/content/content.component';
import { UserProfileResponse } from '../../../../interfaces/types';

@Component({
  selector: 'app-users-list',
  standalone: true,
  imports: [ContentComponent],
  templateUrl: './users-list.component.html',
})
export class UsersListComponent {
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

  AllProfiles!: UserProfileResponse[];
}
