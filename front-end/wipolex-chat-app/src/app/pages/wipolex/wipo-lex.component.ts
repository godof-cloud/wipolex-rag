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
  conversation: any[] = [];

  submitQuestion() {
    this.conversation.push({user: 'User', text: this.question});
    this.displayResult = false;
    this.answer = '';
    this.blocked = true;
    setTimeout(() => {
      // this.uploadService.submitQuestion(this.question).subscribe(
      //   response => {
          this.displayResult = true;
          this.answer = 'test test';
          this.conversation.push({user: 'AI Assistant', text: this.answer});
          this.blocked = false;
          this.question = '';
        // });
    }, 500);
  }

  enterQuestion() {
    this.blocked = true;
    this.displayResult = false;
  }

  get hasConversation() {
    return this.conversation.length > 0;
  }
}
