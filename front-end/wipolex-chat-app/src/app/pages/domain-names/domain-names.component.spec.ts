import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DomainNamesComponent } from './domain-names.component';

describe('DomainNamesComponent', () => {
  let component: DomainNamesComponent;
  let fixture: ComponentFixture<DomainNamesComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ DomainNamesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DomainNamesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
