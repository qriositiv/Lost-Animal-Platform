import { Component, ElementRef, HostListener, OnInit, QueryList, ViewChildren, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { PublicService } from '../../../../../services/public.service';
import { Location } from '../../../../../interfaces/location.interface';
import { Animal } from '../../../../../enums/enums';
import { ReportResponseWithSimilarity, ReportRequestWithSimilarity } from '../../../../../interfaces/report.interface';
import { ReportBoxComponent } from '../../../../shared/report-box/report-box.component';
import { AddressInputComponent } from '../../../../shared/address-input/address-input.component';
import { NgClass } from '@angular/common';
import { ImageUploadComponentNgx } from '../../../../shared/image-upload-ngx/image-upload-ngx.component';
import { ReportPopupComponent } from '../../../../shared/report-popup/report-popup.component';

@Component({
  selector: 'app-search-image',
  templateUrl: './search-image.component.html',
  styleUrls: ['./search-image.component.css'],
  imports: [
    ReportBoxComponent,
    AddressInputComponent,
    ReactiveFormsModule,
    NgClass,
    ImageUploadComponentNgx,
    ReportPopupComponent,
  ],
  standalone: true,
})
export class SearchImageComponent implements OnInit {
  currentStep: string = 'location';
  Animal = Animal;
  quickCompareForm: FormGroup;
  responseData: ReportResponseWithSimilarity[] | null = null;
  isInputFocused: boolean = false;
  selectedReportId: string = '';

  constructor(
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private publicService: PublicService,
    private toastr: ToastrService,
  ) {
    this.quickCompareForm = this.fb.group({
      userLocation: [null, [Validators.required]],
      image: [null, [Validators.required]],
      petType: [null, [Validators.required]],
    });
  }

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe((params) => {
      this.currentStep = params['step'] || 'location';
    });
  }

  onSubmit() {
    const loc = this.quickCompareForm.get('userLocation');
    const img = this.quickCompareForm.get('image');
    if (!loc?.value) {
      this.toastr.error('Missing location', 'ERROR');
      return;
    } else if (!img?.value) {
      this.toastr.error('Missing image', 'ERROR');
      return;
    }

    const req: ReportRequestWithSimilarity = {
      longitude: loc.value.longitude,
      latitude: loc.value.latitude,
      image: img.value,
      petType: this.quickCompareForm.get('petType')?.value,
    };
    this.publicService.quickCompare(req).subscribe({
      next: (response) => {
        this.responseData = response;
        this.currentStep = 'summary';
      },
      error: (error) => {
        this.toastr.error('Failed finding similar images', 'ERROR');
        console.error(error);
      },
    });
  }

  openModal(repSim: ReportResponseWithSimilarity) {
    this.selectedReportId = repSim.report.reportId;
  }

  onCloseModal() {
    this.selectedReportId = '';
  }

  getLocationData(location: Location) {
    this.quickCompareForm.patchValue({
      userLocation: location,
    });
  }

  async onImageUpload(image: string) {
    this.quickCompareForm.patchValue({
      image: image,
    });
  }

  onFocus(): void {
    this.isInputFocused = true;
  }
}
