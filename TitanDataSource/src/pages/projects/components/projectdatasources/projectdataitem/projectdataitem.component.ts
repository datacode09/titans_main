import {Component, Input, OnInit} from '@angular/core';
import {DataSource} from '../../DataSource';


@Component({
  selector: "projectdataitem-root"
  , templateUrl: "./projectdataitem.component.html"
  , styleUrls: ["projectdataitem.component.css"]
})
export class ProjectDataItemComponent implements OnInit {


  @Input()
  public data: DataSource;
  
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