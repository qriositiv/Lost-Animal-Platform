import { Age, Animal, Gender, Size, Type } from "../enums/enums";

export interface Pet {
  petId: string,
  petName: string,
  petType: Animal,
  petBreed: string,
  petSize: Size,
  petGender: Gender,
  petAge: Age,
  petImage: string
}

export interface PetRequest extends PetLostRequest, PetSeenRequest {
  image?: string;
}

export interface PetLostRequest {
  petName: string;
  petType: Animal;
  petSize: Size;
  petGender: Gender;
  petAge: Age;
  breed?: string;
}

export interface PetSeenRequest {
  isTaken: boolean;
  petType: Animal;
  petSize: Size;
  petGender: Gender;
  petAge: Age;
  breed?: string;
}


export interface PetReportRequest {
  petName: string,
  petType: Animal,
  petBreed: string,
  petGender: Gender,
  petSize: Size,
  petAge: Age,
  petImage?: string
}
