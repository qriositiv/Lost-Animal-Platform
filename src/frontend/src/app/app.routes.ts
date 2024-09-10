import { Routes } from '@angular/router';
import { HomeComponent } from './components/public/home/home-page.component';
import { UserProfileComponent } from './components/private/user/user-profile/user-profile.component';
import { UserSettingsComponent } from './components/private/user/user-settings/user-settings.component';
import { PetSummaryComponent } from './components/private/user/user-pets/pet-summary/pet-summary.component';
import { AuthGuard } from './guards/auth.guard';
import { UserReportsComponent } from './components/private/user/user-reports/user-reports.component';
import { RegisterComponent } from './components/public/features/register/register.component';
import { NewReportComponent } from './components/private/new-report/new-report.component';
import { ReportCreatedComponent } from './components/private/new-report/report-created/report-created.component';
import { SearchComponent } from './components/public/features/search/search.component';
import { SearchImageComponent } from './components/public/features/search/search-image/search-image.component';
import { SearchMapComponent } from './components/public/features/search/search-map/search-map.component';
import { TipsComponent } from './components/public/tips/tips-page.component';
import { TipsLostComponent } from './components/public/tips/tips-lost/tips-lost.component';
import { TipsVolunteerComponent } from './components/public/tips/tips-volunteer/tips-volunteer.component';
import { TipsFoundComponent } from './components/public/tips/tips-found/tips-found.component';
import { LoginComponent } from './components/public/features/login/login.component';
import { Page404Component } from './components/public/page-404/page-404.component';
import { SearchReportComponent } from './components/public/features/search/search-reports/search-reports.component';
import { UsersListComponent } from './components/private/moderator-dashboard/users-list/users-list.component';
import { ReportsListComponent } from './components/private/moderator-dashboard/reports-list/reports-list.component';
import { ShelterComponent } from './components/private/user/shelter/shelter.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  // User Profile branch
  { path: ':username/profile', component: UserProfileComponent },
  { path: 'settings', canActivate: [AuthGuard], component: UserSettingsComponent },
  { path: 'new-shelter', canActivate: [AuthGuard], component: PetSummaryComponent },
  { path: ':username/reports', component: UserReportsComponent },
  { path: ':username/shelter', component: ShelterComponent },
  // Home branch
  { path: 'register', component: RegisterComponent },
  { path: 'new-report', canActivate: [AuthGuard], component: NewReportComponent },
  { path: 'submitted', canActivate: [AuthGuard], component: ReportCreatedComponent },
  // Search branch
  { path: 'search', component: SearchComponent },
  { path: 'search-image', component: SearchImageComponent },
  { path: 'search-reports', component: SearchReportComponent },
  { path: 'search-map', component: SearchMapComponent },
  // Tips branch
  { path: 'tips', component: TipsComponent },
  { path: 'tips-lost', component: TipsLostComponent },
  { path: 'tips-found', component: TipsFoundComponent },
  { path: 'tips-volunteer', component: TipsVolunteerComponent },
  // Moderation branch
  { path: 'users-list', component: UsersListComponent },
  { path: 'reports-list', component: ReportsListComponent },

  { path: 'login', component: LoginComponent },
  { path: 'page-404', component: Page404Component },
  // { path: '**', redirectTo: '/page-404' },
];
