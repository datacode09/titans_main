import {Component, OnInit, Input} from '@angular/core';
import {ApiItem} from '../ApiItem';

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

@Component({
  selector: "apiitem-root"
  , templateUrl: "./apiitem.component.html"
  , styleUrls: ["../../../app/app.component.css", "apiitem.component.css"]
})
export class ApiItemComponent implements OnInit {
  @Input()
  public data: ApiItem;
  
  /**
   * 
   */
  public constructor() {
  }
  
  /**
   * 
   */
  public ngOnInit(): void {
  }

}
