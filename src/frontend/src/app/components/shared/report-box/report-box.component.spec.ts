import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportBoxComponent } from './report-box.component';

describe('ReportBoxComponent', () => {
  let component: ReportBoxComponent;
  let fixture: ComponentFixture<ReportBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportBoxComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
