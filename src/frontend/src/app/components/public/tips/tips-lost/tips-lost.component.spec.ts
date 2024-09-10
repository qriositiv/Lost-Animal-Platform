import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipsLostComponent } from './tips-lost.component';

describe('TipsLostComponent', () => {
  let component: TipsLostComponent;
  let fixture: ComponentFixture<TipsLostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipsLostComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TipsLostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
