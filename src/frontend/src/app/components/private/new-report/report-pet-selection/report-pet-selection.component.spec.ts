import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportPetSelectionComponent } from './report-pet-selection.component';

describe('ReportPetSelectionComponent', () => {
  let component: ReportPetSelectionComponent;
  let fixture: ComponentFixture<ReportPetSelectionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportPetSelectionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportPetSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
