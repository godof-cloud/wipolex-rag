import { Component, OnInit } from '@angular/core';
import { LoaderService } from '../../_services/loader.service';

@Component({
  selector: 'app-page-not-found',
  templateUrl: './page-not-found.component.html',
  styleUrls: ['./page-not-found.component.scss']
})
export class PageNotFoundComponent implements OnInit {

  constructor(private loadingService: LoaderService) { }

  ngOnInit(): void {

    
  }

}
