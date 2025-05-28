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
    console.log('call submitQuestion')
      this.uploadService.submitQuestion(this.question,'User').subscribe(
        response => {
          console.log('API call successful!', response.type,response);
          if(response.type === 3 ) {
          this.displayResult = true;
          this.conversation.push({user: 'AI Assistant', text: response.partialText});
          this.blocked = false;
          this.question = '';
          }
        },
        error => {
          console.error('API call failed!', error);
        });
  }

  enterQuestion() {
    this.blocked = true;
    this.displayResult = false;
  }

  get hasConversation() {
    return this.conversation.length > 0;
  }
}
