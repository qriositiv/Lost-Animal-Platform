import { Component, EventEmitter, Input, Output, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import * as L from 'leaflet';
import { Location } from '../../../interfaces/location.interface';
import { Type } from '../../../enums/enums';
import { PublicService } from '../../../services/public.service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-address-input',
  standalone: true,
  imports: [RouterLink, FormsModule, LeafletModule],
  templateUrl: './address-input.component.html',
})
export class AddressInputComponent {
  @Output() locationUpdatedEvent: EventEmitter<Location> = new EventEmitter<Location>();
  private map: L.Map | null = null;
  private marker: L.Marker | null = null;
  location: Location = {
    address: '',
    longitude: 0,
    latitude: 0,
  };
  nextStepAvailable: boolean = false;
  @Input() type!: Type;
  Type = Type;
  private router = inject(Router);
  private publicService = inject(PublicService);
  private subscription: Subscription | null = null;
  private toastr = inject(ToastrService);

  placeholder = $localize`:@@entersearchregion:Enter search region`;

  options = {
    layers: [
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 18,
        minZoom: 3,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      }),
    ],
    zoom: 8,
    // Lithuania as starting position
    center: L.latLng(55.27914, 24.02161),
  };

  goNextStep(): void {
    this.locationUpdatedEvent.emit(this.location);
    if (!this.type) {
      this.router.navigate(['/search-image'], { queryParams: { step: 'image-upload' } });
    } else {
      this.router.navigate(['/new-report'], { queryParams: { type: this.type, step: 'pet-selection' } });
    }
  }

  onMapReady(map: L.Map): void {
    this.map = map;
  }

  searchAddress(): void {
    if (!this.location.address.trim()) return;

    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    this.subscription = this.publicService.searchAddress(this.location.address).subscribe({
      next: (response: any) => {
        if (response && Array.isArray(response) && response[0]) {
          const displayName = response[0].display_name;
          const addressComponents: string[] = displayName.split(',').map((component: string) => component.trim());
          const shortAddress = addressComponents.slice(0, 3).join(', ');

          const { lat, lon } = response[0];

          this.location.latitude = lat;
          this.location.longitude = lon;
          this.location.address = shortAddress;

          if (this.map) {
            this.map.flyTo([lat, lon], 16);
            if (this.marker) {
              this.map.removeLayer(this.marker);
            }
            this.marker = L.marker([lat, lon]).addTo(this.map);
          }
          this.nextStepAvailable = true;
        } else {
          this.toastr.error('Address not found', 'ERROR');
        }
      },
      error: (error) => {
        console.error('Error:', error);
        this.toastr.error('Failed to search the address', 'ERROR');
      },
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
    const { latitude: lat, longitude: lon } = position.coords;
    if (this.map) {
      this.map.flyTo([lat, lon], 14);
      this.location.latitude = lat;
      this.location.longitude = lon;
      if (this.marker) {
        this.map.removeLayer(this.marker);
      }
      this.marker = L.marker([lat, lon]).addTo(this.map);
    }
    this.nextStepAvailable = true;
  }
}
