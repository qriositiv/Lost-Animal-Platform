import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipsFoundComponent } from './tips-found.component';

describe('TipsFoundComponent', () => {
  let component: TipsFoundComponent;
  let fixture: ComponentFixture<TipsFoundComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipsFoundComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TipsFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
