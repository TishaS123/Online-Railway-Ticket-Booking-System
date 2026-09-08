import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageCoachComponent } from './manage-coach.component';

describe('ManageCoachComponent', () => {
  let component: ManageCoachComponent;
  let fixture: ComponentFixture<ManageCoachComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManageCoachComponent]
    });
    fixture = TestBed.createComponent(ManageCoachComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
