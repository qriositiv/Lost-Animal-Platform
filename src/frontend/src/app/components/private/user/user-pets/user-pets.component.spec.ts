import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPetsComponent } from './user-pets.component';

describe('UserPetsComponent', () => {
  let component: UserPetsComponent;
  let fixture: ComponentFixture<UserPetsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserPetsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserPetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
