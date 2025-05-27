import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DomainNamesResponseComponent } from './domain-names-response.component';

describe('DomainNamesResponseComponent', () => {
  let component: DomainNamesResponseComponent;
  let fixture: ComponentFixture<DomainNamesResponseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DomainNamesResponseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DomainNamesResponseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
