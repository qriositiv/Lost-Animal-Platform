import { AbstractControl, ValidatorFn } from '@angular/forms';

export function breedValidator(allowedBreeds: string[]): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const value = control.value ? control.value.toLowerCase() : '';
    const breedsLowerCase = allowedBreeds.map(breed => breed.toLowerCase());
    const forbidden = !breedsLowerCase.includes(value);
    return forbidden ? { forbiddenBreed: { value: control.value } } : null;
  };
}
