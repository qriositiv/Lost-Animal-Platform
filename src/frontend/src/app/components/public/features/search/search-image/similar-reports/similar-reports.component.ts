import { Component, Input } from '@angular/core';
import { ReportBoxComponent } from '../../../../grid-view/content/report-box/report-box.component';

@Component({
  selector: 'app-similar-reports',
  standalone: true,
  imports: [ReportBoxComponent],
  templateUrl: './similar-reports.component.html'
})
export class SimilarReportsComponent {
    @Input() reports!: any;
}
