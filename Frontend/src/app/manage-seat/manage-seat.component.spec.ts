import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageSeatComponent } from './manage-seat.component';

describe('ManageSeatComponent', () => {
  let component: ManageSeatComponent;
  let fixture: ComponentFixture<ManageSeatComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageSeatComponent]
    });
    fixture = TestBed.createComponent(ManageSeatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
