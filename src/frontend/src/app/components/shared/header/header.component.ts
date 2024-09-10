import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { LangDropdownComponent } from './lang-dropdown/lang-dropdown.component';
import { ProfileDropdownComponent } from './profile-dropdown/profile-dropdown.component';
import { MobileMenuComponent } from './mobile-menu/mobile-menu.component';
import { HeaderNavigationComponent } from './header-navigation/header-navigation.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    LangDropdownComponent,
    ProfileDropdownComponent,
    MobileMenuComponent,
    HeaderNavigationComponent,
  ],
  templateUrl: './header.component.html',
})
export class HeaderComponent {
  isMobile: boolean = false;

  constructor() {
    this.checkIfMobile();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: any) {
    this.checkIfMobile();
  }

  checkIfMobile() {
    this.isMobile = window.innerWidth < 768; // Adjust the breakpoint as needed
  }
}
