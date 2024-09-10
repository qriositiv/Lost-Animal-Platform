import { Animal, Type } from '../enums/enums';

export interface Shelter {
  shelter_id: string;
  latitude: number;
  longitude: number;
}

export interface Marker {
  report_id: string;
  latitude: number;
  longitude: number;
  report_type: Type;
  animal_type: Animal;
}

export interface MapResponse {
  mapMarkerResponse: Marker[];
  mapShelterResponse: Shelter[];
}
