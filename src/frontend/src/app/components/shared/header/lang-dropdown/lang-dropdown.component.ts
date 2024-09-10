import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { clickedOutside } from '../../../../directives/clickedOutside.directive';

/* TODO: Presumably, a separate component should be created for this functionality, which will be inherited by other components. */

interface Locale {
  code: string;
  label: string;
  flag: string;
}

@Component({
  selector: 'app-lang-dropdown',
  standalone: true,
  imports: [CommonModule, clickedOutside],
  templateUrl: './lang-dropdown.component.html'
})
export class LangDropdownComponent implements OnInit {
  dropdownOpen: boolean = false;
  // Add new languages here:
  // locales[0] is the default one.
  locales: Locale[] = [
    { code: 'EN', label: 'English', flag: 'gb' },
    { code: 'LT', label: 'Lietuvių', flag: 'lt' },
  ];
  currentLocale = this.locales[0]

  constructor() { }

  ngOnInit(): void {
    if (typeof window !== 'undefined') {
      const currentPath = window.location.pathname;
      const pathSegments = currentPath.split('/');

      const languageCode = pathSegments[1]?.toLowerCase();
      const matchingLocale = this.locales.find(locale => locale.code.toLowerCase() === languageCode);

      this.currentLocale = matchingLocale || this.locales[0];
    }
  }

  generateLink(languageCode: string): string {
    const currentDomain = window.location.origin;
    const currentPath = window.location.pathname;
    const pathSegments = currentPath.split('/');
    pathSegments[1] = languageCode.toLowerCase();
    const fullPath = currentDomain + pathSegments.join('/');
    return fullPath;
  }

  toggleDropdown(): void {
    this.dropdownOpen = !this.dropdownOpen;
  }

  onClickedOutside(): void {
    this.dropdownOpen = false;
  }
}
