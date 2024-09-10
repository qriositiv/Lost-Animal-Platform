import { Contacts } from './contacts.interface';
import { Status, Type } from '../enums/enums';
import { Pet } from './pet.interface';
import { Location } from './location.interface';
import { Comment } from './comment.interface';

export interface Report extends Pet, Location, Contacts {
  petId: string;
  type: Type;
  note: string;
}

export interface ReportFormRequest {
  reportType: Type;
  longitude: number;
  latitude: number;
  address: string;
  petId: string;
  note: string;
}

export interface ReportResponse {
  userId: string;
  reportId: string;
  pet: Pet;
  reportType: Type;
  address: string;
  latitude: number;
  longitude: number;
  note: string;
  createdAt: [number, number, number, number, number, number, number];
}

export interface SingleReportResponse {
  reportId: string;
  user: {
    userId: string;
    username: string;
    firstName: string;
    lastName: string;
    email: string;
    telephone: string;
  };
  pet: Pet;
  reportType: Type;
  reportStatus: Status;
  address: string;
  latitude: number;
  longitude: number;
  note: string;
  createdAt: [number, number, number, number, number, number, number];
}

export interface ReportRequestWithSimilarity {
  longitude: number;
  latitude: number;
  image: string;
  petType: Type;
}

export interface ReportResponseWithSimilarity {
  similarity: number;
  report: ReportResponse;
}
