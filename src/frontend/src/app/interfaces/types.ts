export interface UserCredentialRequest {
  login: string;
  password: string;
}

export interface UserCredentialResponse {
  id: number;
  firstName: string;
  lastName: string;
  login: string;
  token: string;
}

export interface UserRegisterRequest {
  firstName: string;
  lastName: string;
  login: string;
  password: string;
  email: string;
  phone: string;
}

export interface UserProfileResponse {
  userId: string;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  telephone: string;
  role: string;
  totalUserReportCount: number;
  totalUserCommentCount: number;
  createdAt: string;
}

export interface ShelterProfileResponse {
  shelterId: string;
  shelterName: string;
  shelterAddress: string;
  shelterLatitude: string;
  shelterLongitude: string;
  userId: string;
  username: string;
  email: string;
  telephone: string;
  role: string;
  createdAt: [];
}

export interface StatisticResponse {
  userCount: number;
  shelterCount: number;
  reportCount: number;
  foundReportCount: number;
}

export interface JWTdata {
  exp: number;
  firstName: string;
  iat: number;
  id: string;
  lastName: string;
  role: string;
  sub: string;
  username: string;
}

export interface ProfileUpdateRequest {
  image: string;
  email: string;
  firstName: string;
  lastName: string;
  telephone: string;
  username: string;
  userId: number; // Ensure this matches the expected type
}
