import { Component } from '@angular/core';
import { ContentComponent } from '../../../public/features/search/search-reports/content/content.component';

@Component({
  selector: 'app-reports-list',
  standalone: true,
  imports: [ContentComponent],
  templateUrl: './reports-list.component.html',
})
export class ReportsListComponent {}
