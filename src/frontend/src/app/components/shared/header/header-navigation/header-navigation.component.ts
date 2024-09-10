import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { clickedOutside } from '../../../../directives/clickedOutside.directive';

@Component({
  selector: 'app-header-navigation',
  standalone: true,
  imports: [clickedOutside, CommonModule, RouterLink],
  templateUrl: './header-navigation.component.html'
})
export class HeaderNavigationComponent {
  search = $localize`:@@search:Search`;
  resources = $localize`:@@resources:Resources`;

  public categories = [
    {
      description: this.search,
      isDropDownMenu: false,
      route: '/search',
      dropDownTarget: 'searchMenu',
      expanded: false,
      subMenuList: [
        { description: 'Search by Map', route: '/search/map' },
        { description: 'Search all Reports', route: '/search/reports' },
        { description: 'Search by Image', route: '/search/image' },
      ],
    },
    {
      description: this.resources,
      isDropDownMenu: false,
      route: '/tips',
    }
  ];

  toggleDropdown(index: number): void {
    this.categories[index].expanded = !this.categories[index].expanded;
  }

  onClickedOutside(): void {
    for (let i = 0; i < this.categories.length; i++) {
      this.categories[i].expanded = false;
    }
  }
}
