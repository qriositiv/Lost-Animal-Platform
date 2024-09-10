import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimilarReportsComponent } from './similar-reports.component';

describe('SimilarReportsComponent', () => {
  let component: SimilarReportsComponent;
  let fixture: ComponentFixture<SimilarReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SimilarReportsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SimilarReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
