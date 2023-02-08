import {Component, OnInit, Input} from '@angular/core';


@Component({
  selector: 'datasetitem',
  templateUrl: './datasetitem.component.html',
  styleUrls: ['../../../app/app.component.css', './datasetitem.component.css']
})
export class DatasetItemComponent implements OnInit {
  
  @Input()
  public data:any; 
  
  public constructor() {
    
  }
  
  /**
   * 
   */
  public ngOnInit(): void {
  }
}



