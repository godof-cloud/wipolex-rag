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
  ErrorDisplay = false;

  submitQuestion() {
    this.ErrorDisplay = false;
    this.conversation.push({user: 'User', text: this.question});
    this.displayResult = false;
    this.answer = '';
    this.blocked = true;
    console.log('call submitQuestion')
      this.uploadService.submitQuestion(this.question,'User').subscribe(
        response => {
          try {
            // Attempt to parse as JSON
            console.log('API call successful!', response);
            if(response.type === 4 ) {
            console.log("response---",response)
            const jsonResponse = JSON.parse(response?.body);
            console.log('jsonResponse ---',jsonResponse)
            this.displayResult = true;
            this.conversation.push({user: 'AI Assistant', text: jsonResponse.answer,references:jsonResponse.references});
           setTimeout(() => {
            this.blocked = false;
            this.question = '';
           }, 500);
            
            }
          } catch (jsonParseError) {
            this.blocked = false;
            this.ErrorDisplay = true;
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
