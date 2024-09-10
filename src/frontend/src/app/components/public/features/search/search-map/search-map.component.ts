import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { LeafletMarkerClusterModule } from '@asymmetrik/ngx-leaflet-markercluster';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faAngleLeft,
  faAngleRight,
  faCat,
  faCircleCheck,
  faDiamondTurnRight,
  faDog,
  faFeather,
  faLocationCrosshairs,
  faShieldDog,
} from '@fortawesome/free-solid-svg-icons';
import * as L from 'leaflet';
import 'leaflet.markercluster';
import { Router } from '@angular/router';
import { ReportPopupComponent } from '../../../../shared/report-popup/report-popup.component';
import { PublicService } from '../../../../../services/public.service';
import { Animal, Type } from '../../../../../enums/enums';
import { MapResponse, Marker } from '../../../../../interfaces/marker.interface';

@Component({
  selector: 'app-search-map',
  standalone: true,
  imports: [LeafletModule, LeafletMarkerClusterModule, FormsModule, FontAwesomeModule, ReportPopupComponent],
  templateUrl: './search-map.component.html',
})
export class SearchMapComponent implements OnInit {
  constructor(
    private router: Router,
    private apiService: PublicService,
  ) {
    window.scrollTo({
      top: 0,
      behavior: 'smooth',
    });
  }

  placeholder = $localize`:@@entersearchregion:Enter search region`;

  // Filter logic
  expanded: boolean = false;
  showDogs: boolean = true;
  showCats: boolean = true;
  showOther: boolean = false;
  showLost: boolean = true;
  showSeen: boolean = false;
  showFound: boolean = true;
  showShelters: boolean = false;
  faRight = faAngleRight;
  faLeft = faAngleLeft;
  faCheck = faCircleCheck;
  faDog = faDog;
  faCat = faCat;
  faFeather = faFeather;
  faShelter = faShieldDog;
  faLocation = faLocationCrosshairs;
  faMove = faDiamondTurnRight;

  clickedReportId: string | null = null;

  expandFilter(): void {
    this.expanded = !this.expanded;
  }

  switchDogs(): void {
    this.showDogs = !this.showDogs;
    this.getFilterData();
  }

  switchCats(): void {
    this.showCats = !this.showCats;
    this.getFilterData();
  }

  switchOther(): void {
    this.showOther = !this.showOther;
    this.getFilterData();
  }

  switchLost(): void {
    this.showLost = !this.showLost;
    this.getFilterData();
  }

  switchSeen(): void {
    this.showSeen = !this.showSeen;
    this.getFilterData();
  }

  switchFound(): void {
    this.showFound = !this.showFound;
    this.getFilterData();
  }

  switchShelters(): void {
    this.showShelters = !this.showShelters;
    this.getFilterData();
  }

  // Map logic
  private map!: L.Map;
  private markerClusterGroup!: L.MarkerClusterGroup;

  address: string = ''; // Taken from the input field in the templatitudee
  options = {
    layers: [
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        minZoom: 3,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }),
    ],
    zoom: 7,
    center: L.latLng(55.27914, 24.02161),
    worldCopyJump: true,
  };

  markers: MapResponse = { mapMarkerResponse: [], mapShelterResponse: [] };

  ngOnInit() {
    this.apiService.getReportMarker().subscribe({
      next: (response: MapResponse) => {
        this.markers = response;
        this.getFilterData();
      },
      error: (error) => {
        console.error('Error:', error);
        if (error.status === 0) {
          console.error('The API server might be down or there might be a network issue.');
        } else {
          console.error(`Backend returned code ${error.status}, body was: `, error.error);
        }
      },
    });
  }

  onMapReady(map: L.Map) {
    this.map = map;

    this.markerClusterGroup = new L.MarkerClusterGroup();
    this.map.addLayer(this.markerClusterGroup);
  }

  getFilterData(): void {
    if (!this.map) {
      console.error('Map is not initialized yet.');
      return;
    }

    this.markerClusterGroup.clearLayers();

    var filteredAnimals: Marker[] = [];
    if (this.showDogs) {
      filteredAnimals.push(...this.markers.mapMarkerResponse.filter((marker) => marker.animal_type === Animal.Dog));
    }
    if (this.showCats) {
      filteredAnimals.push(...this.markers.mapMarkerResponse.filter((marker) => marker.animal_type === Animal.Cat));
    }
    if (this.showOther) {
      filteredAnimals.push(...this.markers.mapMarkerResponse.filter((marker) => marker.animal_type === Animal.Other));
    }

    var filteredReports: Marker[] = [];
    if (this.showLost) {
      filteredReports.push(...this.markers.mapMarkerResponse.filter((marker) => marker.report_type === Type.Lost));
    }
    if (this.showSeen) {
      filteredReports.push(...this.markers.mapMarkerResponse.filter((marker) => marker.report_type === Type.Seen));
    }
    if (this.showFound) {
      filteredReports.push(...this.markers.mapMarkerResponse.filter((marker) => marker.report_type === Type.Found));
    }

    const filteredMarkers = filteredAnimals.filter((animal) => filteredReports.includes(animal));

    filteredMarkers.forEach((marker) => {
      let iconUrl = '';
      if (marker.report_type === Type.Lost) {
        iconUrl = 'assets/rose_pin.png';
      } else if (marker.report_type === Type.Seen) {
        iconUrl = 'assets/lime_pin.png';
      } else if (marker.report_type === Type.Found) {
        iconUrl = 'assets/indigo_pin.png';
      }

      const leafletMarker = L.marker([marker.latitude, marker.longitude], {
        icon: L.icon({
          iconUrl: iconUrl,
          iconSize: [48, 48],
          iconAnchor: [32, 48], // Adjusted for better positioning
        }),
      });

      leafletMarker.on('click', () => {
        this.clickedReportId = marker.report_id;
      });

      this.markerClusterGroup.addLayer(leafletMarker);
    });

    if (this.showShelters) {
      this.markers.mapShelterResponse.forEach((marker) => {
        let iconUrl = 'assets/shelter_pin.png';

        const leafletMarker = L.marker([marker.latitude, marker.longitude], {
          icon: L.icon({
            iconUrl: iconUrl,
            iconSize: [48, 48],
            iconAnchor: [32, 48],
          }),
        });

        leafletMarker.on('click', () => {
          this.router.navigate([marker.shelter_id, 'shelter']);
        });

        this.markerClusterGroup.addLayer(leafletMarker);
      });
    }
  }

  onCloseModal() {
    this.clickedReportId = null;
  }

  // Search logic
  searchAddress(): void {
    if (!this.address.trim()) return;
    const url = `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(this.address)}`;
    fetch(url)
      .then((response) => response.json())
      .then((data) => {
        if (data && Array.isArray(data)) {
          const { latitude, lon } = data[0];
          this.address = data[0].display_name;
          if (this.map) {
            this.map.flyTo([latitude, lon], 12);
          }
        } else {
          alert('Address not found');
        }
      })
      .catch((error) => {
        console.error('Error:', error);
        alert('Failed to search the address');
      });
  }

  getLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position: GeolocationPosition) => {
          this.showPosition(position);
        },
        (error) => {
          console.error('Error getting location:', error);
        },
      );
    } else {
      console.log('Geolocation is not supported by this browser.');
    }
  }

  showPosition(position: GeolocationPosition) {
    const { latitude: latitude, longitude: longitude } = position.coords;
    if (this.map) {
      this.map.flyTo([latitude, longitude], 12);
    }
  }
}
