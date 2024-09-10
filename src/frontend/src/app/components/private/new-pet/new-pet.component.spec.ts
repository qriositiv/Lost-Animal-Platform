import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewPet } from './report-summary.component';

describe('ReportSummaryComponent', () => {
  let component: NewPet;
  let fixture: ComponentFixture<NewPet>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewPet]
    })
      .compileComponents();

    fixture = TestBed.createComponent(NewPet);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
