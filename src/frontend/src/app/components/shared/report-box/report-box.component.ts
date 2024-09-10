import { Component, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReportResponse, ReportResponseWithSimilarity } from '../../../interfaces/report.interface';
import { Type } from '../../../enums/enums';
import { TranslateService } from '../../../services/translate.service';
import { FormatDateArrayPipe, FormatShortDatePipe } from '../../../pipes/format-date.pipe';

@Component({
  selector: 'app-report-box',
  standalone: true,
  imports: [CommonModule, FormatShortDatePipe, FormatDateArrayPipe],
  templateUrl: './report-box.component.html',
})
export class ReportBoxComponent {
  @Input() report!: ReportResponse;
  @Input() repSim!: ReportResponseWithSimilarity;

  Type = Type;

  translate = inject(TranslateService);
}
