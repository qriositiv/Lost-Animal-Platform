import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSettingsOtherComponent } from './user-settings-other.component';

describe('UserSettingsOtherComponent', () => {
  let component: UserSettingsOtherComponent;
  let fixture: ComponentFixture<UserSettingsOtherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSettingsOtherComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserSettingsOtherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
