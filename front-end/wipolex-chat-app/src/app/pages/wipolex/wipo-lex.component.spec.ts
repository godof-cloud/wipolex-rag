import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { WipoLexComponent } from './wipo-lex.component';

describe('WipoLexComponent', () => {
  let component: WipoLexComponent;
  let fixture: ComponentFixture<WipoLexComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ WipoLexComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WipoLexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
