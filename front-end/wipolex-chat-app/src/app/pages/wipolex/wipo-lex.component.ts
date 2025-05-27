import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-wipo-chat',
  templateUrl: './wipo-lex.component.html',
  styleUrls: ['./wipo-lex.component.scss']
})
export class WipoLexComponent {

  constructor() { }
  question :string = '';
  answer :string = '';
  blocked = false;
  displayResult = false;

  submitQuestion() {
    this.blocked = true;
    this.displayResult = true;
    this.question = 'What is your name?'
    this.answer = 'Tarun';
    this.blocked = false;
  }
}
