import { Component, EventEmitter, inject, OnInit, Output } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { Animal, Gender } from "../../../../../../enums/enums";
import { debounceTime, Subject, Subscription } from "rxjs";
import { PublicService } from "../../../../../../services/public.service";
import { ReportResponse } from "../../../../../../interfaces/report.interface";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-filter",
  standalone: true,
  imports: [FormsModule],
  templateUrl: "./filter.component.html"
})
export class FilterComponent implements OnInit {
  @Output() filteredReports = new EventEmitter<Array<ReportResponse>>();
  newBreed = "";
  selectedOptions: any = {
    page: 1,
    reportTypeList: ["LOST", "SEEN", "FOUND"],
    reportLocationRequest: {
      latitude: null,
      longitude: null,
      distance: 100
    },
    reportPublishHourRange: null,
    petDataRequest: {
      petTypeList: ["DOG", "CAT", "OTHER"],
      petBreedList: [],
      petGenderList: ["MALE", "FEMALE"]
    },
    reportCreator: null
  };
  lostChecked: boolean = this.selectedOptions.reportTypeList.includes("LOST");
  foundChecked: boolean = this.selectedOptions.reportTypeList.includes("FOUND");
  seenChecked: boolean = this.selectedOptions.reportTypeList.includes("SEEN");
  dogChecked: boolean = this.selectedOptions.petDataRequest.petTypeList.includes("DOG");
  catChecked: boolean = this.selectedOptions.petDataRequest.petTypeList.includes("CAT");
  otherChecked: boolean = this.selectedOptions.petDataRequest.petTypeList.includes("OTHER");
  maleChecked: boolean = this.selectedOptions.petDataRequest.petGenderList.includes("MALE");
  femaleChecked: boolean = this.selectedOptions.petDataRequest.petGenderList.includes("FEMALE");
  private filterChanged = new Subject<void>();
  private creatorInputChange = new Subject<void>();
  private checkboxChange = new Subject<void>();


  service = inject(PublicService);
  private subscription: Subscription | null = null;
  private toastr = inject(ToastrService);

  constructor() {
    this.filterChanged.pipe(debounceTime(700)).subscribe(() => {
      this.applyFilter();
    });

    this.creatorInputChange.pipe(debounceTime(1000)).subscribe(() => {
      this.applyFilter();
    });

    this.checkboxChange.pipe(debounceTime(500)).subscribe(() => {
      this.applyFilter();
    });
  }

  ngOnInit() {
    this.applyFilter();
  }

  applyFilter() {
    this.service.getFilteredReports(this.selectedOptions).subscribe((reports: Array<ReportResponse>) => {
      this.filteredReports.emit(reports);
    });
  }

  onFilterChange() {
    this.filterChanged.next();
  }

  onCheckboxChange(event: any) {
    const { name, checked } = event.target;

    if (["LOST", "FOUND", "SEEN"].includes(name)) {
      if (checked && !this.selectedOptions.reportTypeList.includes(name)) {
        this.selectedOptions.reportTypeList.push(name);
      } else if (!checked) {
        const index = this.selectedOptions.reportTypeList.indexOf(name);
        if (index !== -1) {
          this.selectedOptions.reportTypeList.splice(index, 1);
        }
      }
    }


    if (Object.values(Animal).includes(name)) {
      if (checked && !this.selectedOptions.petDataRequest.petTypeList.includes(name)) {
        this.selectedOptions.petDataRequest.petTypeList.push(name);
      } else if (!checked) {
        const index = this.selectedOptions.petDataRequest.petTypeList.indexOf(name);
        if (index !== -1) {
          this.selectedOptions.petDataRequest.petTypeList.splice(index, 1);
        }
      }
    }


    if (Object.values(Gender).includes(name)) {
      if (checked && !this.selectedOptions.petDataRequest.petGenderList.includes(name)) {
        this.selectedOptions.petDataRequest.petGenderList.push(name);
      } else if (!checked) {
        const index = this.selectedOptions.petDataRequest.petGenderList.indexOf(name);
        if (index !== -1) {
          this.selectedOptions.petDataRequest.petGenderList.splice(index, 1);
        }
      }
    }

    this.checkboxChange.next();
  }

  onLocationInputChange() {
    this.subscription = this.service.searchAddress(this.selectedOptions.location).subscribe({
      next: (response: any) => {
        if (response && Array.isArray(response) && response[0]) {
          const { lat, lon } = response[0];

          this.selectedOptions.reportLocationRequest.latitude = lat;
          this.selectedOptions.reportLocationRequest.longitude = lon;

          this.onFilterChange();
        } else {
          this.toastr.error("Address not found", "ERROR");
        }
      },
      error: (error) => {
        console.error("Error:", error);
        this.toastr.error("Failed to search the address", "ERROR");
      }
    });
  }

  onCreatorChange(event: any) {
    this.selectedOptions.reportCreator = event.target.value;
    this.creatorInputChange.next();
  }

  onRangeChange(event: any, name: string) {
    if (name == "distance") {
      this.selectedOptions.reportLocationRequest.distance = event.target.value;
    } else {
      this.selectedOptions.reportPublishHourRange = event.target.value;
      this.onFilterChange();
    }
  }

  addBreed() {
    if (this.newBreed.trim()) {
      this.selectedOptions.petDataRequest.petBreedList.push(this.newBreed.trim());
      this.newBreed = "";
      this.applyFilter();
    }
  }

  removeBreed(breedToRemove: string) {
    const index = this.selectedOptions.petDataRequest.petBreedList.indexOf(breedToRemove);
    if (index > -1) {
      this.selectedOptions.petDataRequest.petBreedList.splice(index, 1);
      this.applyFilter();
    }
  }
}
