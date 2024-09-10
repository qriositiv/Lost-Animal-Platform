import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Input, LOCALE_ID, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import * as L from 'leaflet';
import { Age, Animal, Gender, Size, Status, Type } from '../../../enums/enums';
import { SingleReportResponse } from '../../../interfaces/report.interface';
import { CapitalizeFirstPipe } from '../../../pipes/capitalize-first.pipe';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBan, faCircleCheck, faCircleXmark, faLanguage, faTrash } from '@fortawesome/free-solid-svg-icons';
import { CommentsComponent } from '../../public/features/search/comments/comments.component';
import { PublicService } from '../../../services/public.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '../../../services/translate.service';
import { CommentResponse } from '../../../interfaces/comment.interface';
import { AuthService } from '../../../services/auth.service';
import { NewReportService } from '../../../services/new-report.service';
import { FormatDateArrayPipe, FormatShortDatePipe } from '../../../pipes/format-date.pipe';
import { ConfirmationPopupComponent } from '../confirmation-popup/confirmation-popup.component';
import { Router, RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-report-popup',
  standalone: true,
  imports: [
    CommonModule,
    LeafletModule,
    FormsModule,
    CapitalizeFirstPipe,
    FontAwesomeModule,
    CommentsComponent,
    FormatDateArrayPipe,
    FormatShortDatePipe,
    ConfirmationPopupComponent,
    RouterModule,
  ],
  templateUrl: './report-popup.component.html',
})
export class ReportPopupComponent implements OnInit {
  @Output() closeModal: EventEmitter<void> = new EventEmitter<void>();
  @Input() reportId!: string;

  faCircleCheck = faCircleCheck;
  faTrash = faTrash;
  faCircleClose = faCircleXmark;
  faTranslate = faLanguage;
  faBlock = faBan;
  translated: boolean = false;

  Type = Type;
  Animal = Animal;
  Size = Size;
  Gender = Gender;
  Age = Age;

  report: SingleReportResponse;
  publicService = inject(PublicService);
  toastr = inject(ToastrService);
  translate = inject(TranslateService);

  allComments!: Array<CommentResponse>;
  reportService = inject(NewReportService);
  authService = inject(AuthService);
  locale: string = inject(LOCALE_ID);
  translatedKeep: string = '';
  publicTranslateSubscription: Subscription | null = null;
  user!: string;
  role!: string;

  constructor(private router: Router) {
    this.report = {
      reportId: '',
      user: {
        userId: '',
        username: '',
        firstName: '',
        lastName: '',
        email: '',
        telephone: '',
      },
      pet: {
        petId: '0',
        petName: '',
        petAge: Age.Unknown,
        petSize: Size.Unknown,
        petGender: Gender.Unknown,
        petType: Animal.Unknown,
        petBreed: '',
        petImage: '',
      },
      reportType: Type.Lost,
      reportStatus: Status.Unknown,
      address: '',
      longitude: 0,
      latitude: 0,
      note: '',
      createdAt: [0, 0, 0, 0, 0, 0, 0],
    };
  }

  ngOnInit(): void {
    this.publicService.getReportById(this.reportId).subscribe({
      next: (response: SingleReportResponse) => {
        this.report = response;
        if (this.map) {
          this.onMapReady(this.map);
        }
      },
      error: (error) => {
        this.toastr.error('Failed to fetch report', 'ERROR');
        console.log(error);
      },
    });

    this.publicService.getAllCommentsByReportId(this.reportId).subscribe({
      next: (response: Array<CommentResponse>) => {
        this.allComments = response;
      },
      error: (error) => {
        this.toastr.error('Failed to fetch comments', 'ERROR');
        console.log(error);
      },
    });

    const token = this.authService.decodeToken();
    if (!token) return;

    const username = token.username;
    this.user = token.username;
    this.role = token.role;
  }

  private map: L.Map | null = null;
  private marker: L.Marker | null = null;
  options = {
    layers: [
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        minZoom: 3,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }),
    ],
    zoom: 8,
    center: L.latLng(55.27914, 24.02161),
  };

  onMapReady(map: L.Map): void {
    this.map = map;
    if (this.marker) {
      this.marker.addTo(this.map);
    }
    if (this.map) {
      this.map.setView([this.report.latitude, this.report.longitude], 12);
      if (this.marker) {
        this.map.removeLayer(this.marker);
      }
      this.marker = L.marker([this.report.latitude, this.report.longitude]).addTo(this.map);
    }
  }

  close() {
    this.closeModal.emit();
  }

  translateNotes(note: string): void {
    this.translated = !this.translated;
    if (this.translatedKeep.length != 0) {
      const temp = note;
      note = this.translatedKeep;
      this.translatedKeep = temp;
    } else {
      this.translatedKeep = note;
      this.publicTranslateSubscription = this.publicService.translate(note, 'auto', this.locale).subscribe({
        next: (response) => {
          note = response.translatedText;
        },
        error: (error) => {
          this.toastr.error('Failed fetching ' + error.error.text, 'Error!');
          console.log(error);
        },
      });
    }
  }

  onUpdateReport(reportId: string): void {
    this.reportService.updateReport(reportId).subscribe({
      next: () => {
        this.toastr.success('Report updated successfully', 'Success');
        this.ngOnInit();
      },
      error: (err) => {
        console.error('Error updating report:', err);
        this.toastr.error('Error updating the report', 'Error');
      },
    });
  }

  onDeleteReport(reportId: string): void {
    this.reportService.deleteReport(reportId).subscribe({
      next: () => {
        this.toastr.success('Report deleted successfully', 'Success');
        this.close();
      },
      error: (err) => {
        console.error('Error deleting report:', err);
        this.toastr.error('Error deleting the report', 'Error');
      },
    });
  }

  onBlockReport(reportId: string): void {
    this.reportService.blockReport(reportId).subscribe({
      next: () => {
        this.toastr.success('Report blocking successfully', 'Success');
      },
      error: (error) => {
        this.toastr.error('Error blocking the report', 'Error');
      },
    });
  }

  onAuthorClick() {
    this.router.navigate([`/${this.report.user.username}/profile/`]);
  }

  protected readonly Status = Status;

  isModalVisible: boolean = false;
  selectedReportId: string | undefined;

  showModal(petId: string) {
    this.selectedReportId = petId;
    this.isModalVisible = true;
  }

  _closeModal() {
    this.isModalVisible = false;
    this.selectedReportId = undefined;
  }
}
