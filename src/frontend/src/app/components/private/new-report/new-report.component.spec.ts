import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LostPetPageComponent } from './lost-pet-page.component';

describe('LostPetPageComponent', () => {
  let component: LostPetPageComponent;
  let fixture: ComponentFixture<LostPetPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LostPetPageComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LostPetPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
