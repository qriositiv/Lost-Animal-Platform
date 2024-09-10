import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSettingsProfileComponent } from './user-settings-profile.component';

describe('UserSettingsProfileComponent', () => {
  let component: UserSettingsProfileComponent;
  let fixture: ComponentFixture<UserSettingsProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSettingsProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserSettingsProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
