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
            // console.log("response---",response)
            // const jsonResponse = JSON.parse(this.cleanStringValue(response?.body));
            // console.log('jsonResponse ---',jsonResponse)
            this.displayResult = true;
            this.conversation.push({user: 'AI Assistant', text: response?.body});
            // if(jsonResponse.references.length > 0) {  this.conversation.push({references:jsonResponse.references}); }
           setTimeout(() => {
            this.blocked = false;
            this.question = '';
           }, 500);
            
            }
          } catch (jsonParseError) {
            this.blocked = false;
            setTimeout(() => {
              this.ErrorDisplay = false;
             }, 1500);
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

   cleanStringValue(text: string): string {
    // Define regex patterns outside the function for efficiency
// Matches common Unicode bullets, asterisks, and hyphens (as list markers)
const bulletRegex = /[*•‣⁃●▶►▪-]/g; // hyphen needs to be at end or escaped in character class
// Matches one or more whitespace characters (including newlines, tabs, spaces)
const multipleSpacesRegex = /\s\s+/g;
    // 1. Remove bullet characters
    let cleanedText = text.replace(bulletRegex, '');
  
    // 2. Replace multiple spaces (including newlines, tabs) with a single space
    cleanedText = cleanedText.replace(multipleSpacesRegex, ' ');

    cleanedText = cleanedText.replace('""', '\'');
  
    // 3. Trim leading/trailing whitespace
    cleanedText = cleanedText.trim();
  
    return cleanedText;
  }
}
