import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditFareComponent } from './edit-fare.component';

describe('EditFareComponent', () => {
  let component: EditFareComponent;
  let fixture: ComponentFixture<EditFareComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditFareComponent]
    });
    fixture = TestBed.createComponent(EditFareComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
