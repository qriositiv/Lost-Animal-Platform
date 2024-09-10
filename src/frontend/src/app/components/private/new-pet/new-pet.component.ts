import { Component, ElementRef, HostListener, Input, OnDestroy, QueryList, ViewChildren, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Age, Animal, Gender, Size, Type } from '../../../enums/enums';
import { Router, RouterLink } from '@angular/router';
import { breedValidator } from '../../../validators/pet_breed_validator';
import { DOG_BREEDS } from '../../../../assets/dog-breeds';
import { CAT_BREEDS } from '../../../../assets/cat-breeds';
import { OTHER_BREEDS } from '../../../../assets/other-breeds';
import { CommonModule } from '@angular/common';
import { ImageUploadComponentNgx } from '../../shared/image-upload-ngx/image-upload-ngx.component';
import { NewPetService } from '../../../services/new-pet.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-new-pet',
  standalone: true,
  imports: [RouterLink, CommonModule, ReactiveFormsModule, ImageUploadComponentNgx],
  templateUrl: './new-pet.component.html',
  styleUrl: './new-pet.component.css',
})
export class NewPetComponent implements OnDestroy {
  @ViewChildren('breedItem') breedItems!: QueryList<ElementRef>;
  @Input() type!: Type;
  service = inject(NewPetService);
  router = inject(Router);
  toastr = inject(ToastrService);
  private subscription: Subscription | undefined;

  Type = Type;
  Animal = Animal;
  Size = Size;
  Gender = Gender;
  Age = Age;
  petForm!: FormGroup;
  breedArray: string[] = [];

  constructor(private fb: FormBuilder) {
    this.petForm = this.fb.group({
      pet_found: [false],
      pet_name: ['', [Validators.minLength(2), Validators.maxLength(30)]],
      pet_type: [null, [Validators.required]],
      pet_size: [null, [Validators.required]],
      pet_gender: [null, [Validators.required]],
      pet_age: [null, [Validators.required]],
      pet_breed: ['', [Validators.required, breedValidator([])]],
      image: ['', [Validators.required]],
    });

    this.petForm.get('pet_breed')?.valueChanges.subscribe((value) => {
      this.filterBreeds(value);
    });

    this.petForm.get('pet_found')?.valueChanges.subscribe((value) => {
      this.onCheckboxChange(value);
    });

    this.petForm.get('pet_type')?.valueChanges.subscribe((petType) => {
      const breedInput = this.petForm.get('pet_breed');

      if (petType === Animal.Dog) {
        breedInput?.setValidators([Validators.required, breedValidator(DOG_BREEDS)]);
        this.breedArray = DOG_BREEDS;
        breedInput?.enable();
        this.petForm.patchValue({
          pet_breed: '',
        });
      } else if (petType === Animal.Cat) {
        breedInput?.setValidators([Validators.required, breedValidator(CAT_BREEDS)]);
        this.breedArray = CAT_BREEDS;
        breedInput?.enable();
        this.petForm.patchValue({
          pet_breed: '',
        });
      } else {
        breedInput?.setValidators([Validators.required, breedValidator(OTHER_BREEDS)]);
        this.breedArray = OTHER_BREEDS;
        this.petForm.patchValue({
          pet_breed: 'Other',
        });
        breedInput?.disable();
      }
      breedInput?.updateValueAndValidity();
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onCheckboxChange(isFoundPet: boolean): void {
    if (isFoundPet) {
      this.type = Type.Found;
    } else {
      this.type = Type.Seen;
    }
  }

  filteredBreeds: string[] = [];
  highlightedIndex: number = -1;
  isInputFocused: boolean = false;

  async onImageUpload(image: string) {
    this.petForm.patchValue({
      image: image,
    });
  }

  onSubmit(): void {
    this.subscription = this.service.createPet(this.petForm, this.type).subscribe({
      next: (response) => {
        this.toastr.success('You have sucessfully added a pet.', 'Success!');
        this.router.navigate(['/new-report'], {
          queryParams: {
            type: this.type,
            step: 'pet-selection',
          },
        });
      },
      error: (error) => {
        this.toastr.error('Failed adding a pet' + error.error.text, 'Error!');
        console.log(error);
      },
    });
  }

  @HostListener('window:keydown', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent): void {
    if (event.key === 'ArrowDown') {
      if (this.highlightedIndex < this.filteredBreeds.length - 1) {
        this.highlightedIndex++;
        this.scrollToHighlightedItem();
      }
      event.preventDefault();
    } else if (event.key === 'ArrowUp') {
      if (this.highlightedIndex > 0) {
        this.highlightedIndex--;
        this.scrollToHighlightedItem();
      }
      event.preventDefault();
    } else if (event.key === 'Enter' && this.highlightedIndex >= 0) {
      this.selectBreed(this.filteredBreeds[this.highlightedIndex]);
      event.preventDefault();
    } else if (event.key === 'Escape') {
      this.filteredBreeds = [];
      this.highlightedIndex = -1;
      event.preventDefault();
    }
  }

  filterBreeds(value: string): void {
    const input = value.toLowerCase();
    this.filteredBreeds = this.breedArray.filter((breed) => breed.toLowerCase().includes(input));
  }

  private scrollToHighlightedItem(): void {
    if (this.highlightedIndex >= 0 && this.highlightedIndex < this.breedItems.length) {
      this.breedItems
        .toArray()
        [this.highlightedIndex].nativeElement.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
    }
  }

  selectBreed(breed: string): void {
    this.petForm.get('pet_breed')?.setValue(breed);
    this.filteredBreeds = [];
    this.highlightedIndex = -1;
  }

  onFocus(): void {
    this.isInputFocused = true;
  }

  onBlur(): void {
    setTimeout(() => {
      this.filteredBreeds = [];
      this.highlightedIndex = -1;
      this.isInputFocused = false;
    }, 200); // Delay to allow click event to register
  }
}
