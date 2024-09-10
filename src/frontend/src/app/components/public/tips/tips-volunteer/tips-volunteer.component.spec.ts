import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipsVolunteerComponent } from './tips-volunteer.component';

describe('TipsVolunteerComponent', () => {
  let component: TipsVolunteerComponent;
  let fixture: ComponentFixture<TipsVolunteerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipsVolunteerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TipsVolunteerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
