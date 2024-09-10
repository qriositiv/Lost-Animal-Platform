import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PetSummaryComponent } from './pet-summary.component';

describe('PetSummaryComponent', () => {
  let component: PetSummaryComponent;
  let fixture: ComponentFixture<PetSummaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PetSummaryComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PetSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
