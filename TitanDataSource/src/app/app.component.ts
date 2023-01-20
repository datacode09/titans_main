import {Component, OnInit} from '@angular/core';

declare var dojo: any;
declare var esri: any;
declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'titans.datasource';
  
  /**
   * 
   */
  public ngOnInit(): void {
  }
}
