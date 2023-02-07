import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

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
  
  
  public constructor(private router:Router) {
  }
  /**
   * 
   */
  public ngOnInit(): void {
    this.router.navigateByUrl("main", {skipLocationChange:true}); 
  }
}
