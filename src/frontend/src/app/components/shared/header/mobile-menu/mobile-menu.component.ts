import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { clickedOutside } from '../../../../directives/clickedOutside.directive';

@Component({
  selector: 'app-mobile-menu',
  standalone: true,
  imports: [CommonModule, clickedOutside, RouterLink, FontAwesomeModule],
  templateUrl: './mobile-menu.component.html'
})
export class MobileMenuComponent {
  dropdownOpen: boolean = false;

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onClickedOutside(): void {
    this.dropdownOpen = false;
  }

  faBars = faBars;
}
