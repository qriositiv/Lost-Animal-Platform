import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { faShieldDog } from '@fortawesome/free-solid-svg-icons';
import { faBullhorn } from '@fortawesome/free-solid-svg-icons';
import { faCircleCheck } from '@fortawesome/free-solid-svg-icons';
import { PublicService } from '../../../../services/public.service';
import { StatisticResponse } from '../../../../interfaces/types';
import { Subscription } from 'rxjs';

@Component({
  selector: 'statistic',
  standalone: true,
  imports: [FaIconComponent],
  templateUrl: './statistic.component.html'
})
export class StatisticComponent implements OnInit, OnDestroy {
  faUser = faUser;
  faShieldDog = faShieldDog;
  faBullhorn = faBullhorn;
  faCircleCheck = faCircleCheck;
  subscription = new Subscription();

  userCount: number = 0;
  shelterCount: number = 0;
  reportCount: number = 0;
  foundReportCount: number = 0;
  successPercent: number = 0;

  service = inject(PublicService);

  ngOnInit() {
    this.subscription = this.service.getStatistic().subscribe({
      next: (response: StatisticResponse) => {
        this.userCount = response.userCount;
        this.shelterCount = response.shelterCount;
        this.reportCount = response.reportCount;
        this.foundReportCount = response.foundReportCount;
        this.successPercent = this.foundReportCount ? Math.round((this.foundReportCount * 100) / this.reportCount) : 0;
      },
      error: (error) => {
        console.error("ERROR FETCHING STATISTICS:", error);
      }
    });
  }
  ngOnDestroy() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
