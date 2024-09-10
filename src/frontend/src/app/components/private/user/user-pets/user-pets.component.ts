import { Component, EventEmitter, inject, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AsyncPipe, CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCheck } from '@fortawesome/free-solid-svg-icons';
import { Observable, of, Subscription } from 'rxjs';
import { PublicService } from '../../../../services/public.service';
import { Pet } from '../../../../interfaces/pet.interface';
import { TranslateService } from '../../../../services/translate.service';
import { ConfirmationPopupComponent } from '../../../shared/confirmation-popup/confirmation-popup.component';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../../../services/auth.service';

@Component({
  selector: 'app-user-pets',
  standalone: true,
  imports: [RouterLink, FontAwesomeModule, AsyncPipe, CommonModule, ConfirmationPopupComponent],
  templateUrl: './user-pets.component.html',
})
export class UserPetsComponent implements OnInit, OnDestroy {
  @Input() editable: any;
  @Input() username!: string;
  @Output() buttonClicked: EventEmitter<string> = new EventEmitter<string>();
  service = inject(PublicService);
  authService = inject(AuthService);
  translate = inject(TranslateService);
  toastr = inject(ToastrService);
  petList$: Observable<Pet[]> = of();
  isModalVisible: boolean = false;
  selectedPetId: string | undefined;
  private subscriptions: Subscription = new Subscription();
  isAuthor: boolean = false;

  ngOnInit(): void {
    this.loadPetList();
  }
  ngOnDestroy() {
    if (this.subscriptions) {
      this.subscriptions.unsubscribe();
    }
  }

  loadPetList() {
    const token = this.authService.decodeToken();
    if (this.username) {
      this.petList$ = this.service.getPetListByUsername(this.username);
      if (token) {
        if (token.username = this.username) {
          this.isAuthor = true;
        }
      }
      this.subscriptions = this.petList$.pipe().subscribe({
        error: (err) => {
          console.error('Error:', err);
        },
      });
    } else {
      if (!token) return;
      this.isAuthor = true;
      this.petList$ = this.service.getPetListByUsername(token.username);
      this.subscriptions = this.petList$.pipe().subscribe({
        error: (err) => {
          console.error('Error:', err);
        },
      });
    }
  }

  showModal(petId: string) {
    this.selectedPetId = petId;
    this.isModalVisible = true;
  }

  closeModal() {
    this.isModalVisible = false;
    this.selectedPetId = undefined;
  }

  deletePet(confirm: boolean): void {
    if (confirm && this.selectedPetId) {
      this.service.deletePet(this.selectedPetId).subscribe({
        next: (response) => {
          this.loadPetList();
          this.toastr.success('Pet deleted successfully', 'Success!');
        },
        error: (err) => {
          this.toastr.error('Error deleting pet', 'Error!');
          console.error('Error:', err);
        },
      });
    }
    this.closeModal();
  }

  faCheck = faCheck;
  isSelected: boolean = false;
  petSelected: number = -1;
  pet_id: number = 123;

  onButtonClick(index: number, select: boolean, petId: string): void {
    this.buttonClicked.emit(petId);
    this.isSelected = select;
    if (this.isSelected) {
      this.petSelected = index;
    } else {
      this.petSelected = -1;
    }
  }
}
