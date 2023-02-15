import {Component, Input, OnInit, OnDestroy} from '@angular/core';
import {DataSource} from '../../DataSource';


@Component({
  selector: "projectdataitem-root"
  , templateUrl: "./projectdataitem.component.html"
  , styleUrls: ["projectdataitem.component.css"]
})
export class ProjectDataItemComponent implements OnInit, OnDestroy {

  @Input()
  public data: DataSource;  
  public selected = true;  
  
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
  
  /**
   * 
   */
  public onSelectedChanged(evt:any) {
    this.selected = !this.selected
  }
  
  /**
   * 
   */
  public ngOnDestroy(): void {
    
  }
}