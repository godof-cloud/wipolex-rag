import { Component, OnInit } from '@angular/core';
import { UploadService } from 'src/app/_services/upload.service';

@Component({
  selector: 'app-wipo-chat',
  templateUrl: './wipo-lex.component.html',
  styleUrls: ['./wipo-lex.component.scss']
})
export class WipoLexComponent {

  constructor(private uploadService: UploadService) { }
  question: string = '';
  answer: string = '';
  blocked = false;
  displayResult = false;

  submitQuestion() {
    this.displayResult = false;
    this.answer = '';
    this.blocked = true;
    setTimeout(() => {
      // this.uploadService.submitQuestion(this.question).subscribe(
      //   response => {
          this.displayResult = true;
          this.question = 'What is your name?'
          this.answer = 'test test';
          this.blocked = false;
        // });
    }, 500);

  }
}
