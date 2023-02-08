import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import { Location } from '@angular/common';

declare var dojo: any;
declare var esri: any;
declare var $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['../app/app.component.css']
})
export class AppComponent implements OnInit {
  title = 'titans.datasource';
  
  
  public constructor(private router:Router, private location:Location) {
  }
  /**
   * 
   */
  public ngOnInit(): void {
    console.log(this.location.path());
    const currpath = this.location.path();
    if (currpath === "" || currpath === "/") {
      this.router.navigateByUrl("main", {skipLocationChange:true}); 
    }
  }
}
