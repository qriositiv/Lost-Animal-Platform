import { Component } from '@angular/core';
import { FilterComponent } from './filter/filter.component';
import { ContentComponent } from './content/content.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faFilter, faSort } from '@fortawesome/free-solid-svg-icons';
import { ReportResponse } from "../../../../../interfaces/report.interface";

@Component({
  selector: 'app-search-report',
  standalone: true,
  imports: [
    FilterComponent,
    ContentComponent,
    FontAwesomeModule
  ],
  templateUrl: './search-reports.component.html',
})
export class SearchReportComponent {
  enterSearchRegion = $localize`:@@enterSearchRegionHere:Enter search region here`;
  filteredReports: Array<ReportResponse> | null = null;

  receiveFilteredReports(reports: Array<ReportResponse>) {
    this.filteredReports = reports;
  }
  faFilter = faFilter;
  faSort = faSort;
}
