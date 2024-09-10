import { Injectable } from '@angular/core';
import { Age, Animal, Gender, Size, Type } from '../enums/enums';

@Injectable({
  providedIn: 'root',
})
export class TranslateService {
  TypeTranslations = {
    [Type.Lost]: $localize`:@@lost:LOST`,
    [Type.Seen]: $localize`:@@seen:SEEN`,
    [Type.Found]: $localize`:@@found:FOUND`,
    [Type.Unknown]: $localize`:@@unknown:UNKNOWN`,
  };

  getTypeTranslation(type: Type): string {
    return this.TypeTranslations[type];
  }

  AnimalTranslations = {
    [Animal.Dog]: $localize`:@@dog:DOG`,
    [Animal.Cat]: $localize`:@@cat:CAT`,
    [Animal.Other]: $localize`:@@other:OTHER`,
    [Animal.Unknown]: $localize`:@@unknown:UNKNOWN`,
  };

  getAnimalTranslation(animal: Animal): string {
    return this.AnimalTranslations[animal];
  }

  SizeTranslations = {
    [Size.Small]: $localize`:@@small:SMALL`,
    [Size.Medium]: $localize`:@@medium:MEDIUM`,
    [Size.Large]: $localize`:@@large:LARGE`,
    [Size.Unknown]: $localize`:@@unknown:UNKNOWN`,
  };

  getSizeTranslation(size: Size): string {
    return this.SizeTranslations[size];
  }

  GenderTranslations = {
    [Gender.Male]: $localize`:@@male:MALE`,
    [Gender.Female]: $localize`:@@female:FEMALE`,
    [Gender.Unknown]: $localize`:@@unknown:UNKNOWN`,
  };

  getGenderTranslation(gender: Gender): string {
    return this.GenderTranslations[gender];
  }

  AgeTranslations = {
    [Age.Young]: $localize`:@@young:YOUNG`,
    [Age.Adult]: $localize`:@@adult:ADULT`,
    [Age.Senior]: $localize`:@@senior:SENIOR`,
    [Age.Unknown]: $localize`:@@unknown:UNKNOWN`,
  };

  getAgeTranslation(age: Age): string {
    return this.AgeTranslations[age];
  }
}
