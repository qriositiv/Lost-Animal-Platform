import { CommonModule } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { ReportBoxComponent } from '../../../../../shared/report-box/report-box.component';
import { ReportPopupComponent } from '../../../../../shared/report-popup/report-popup.component';
import { ReportResponse } from '../../../../../../interfaces/report.interface';
import { PublicService } from '../../../../../../services/public.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-content',
  standalone: true,
  imports: [CommonModule, ReportBoxComponent, ReportPopupComponent],
  templateUrl: './content.component.html',
})
export class ContentComponent {
  @Input() author!: string;
  @Input() allReports: Array<ReportResponse> | null = null;
  selectedReportId: string = '';
  publicService = inject(PublicService);
  toastr = inject(ToastrService);

  ngOnInit() {
    this.fetchReports();
  }

  fetchReports() {
    if (this.author) {
      this.publicService.getAllReportsByUserId(this.author).subscribe({
        next: (response: Array<ReportResponse>) => {
          this.allReports = response;
        },
        error: (error) => {
          this.toastr.error('Failed to fetch report by author', 'ERROR');
          console.log(error);
        },
      });
    } else {
      this.publicService.getAllReports().subscribe({
        next: (response: Array<ReportResponse>) => {
          this.allReports = response;
        },
        error: (error) => {
          this.toastr.error('Failed to fetch all reports', 'ERROR');
          console.log(error);
        },
      });
    }
  }

  openModal(report: ReportResponse) {
    this.selectedReportId = report.reportId;
  }

  onCloseModal() {
    this.selectedReportId = '';
  }
}
