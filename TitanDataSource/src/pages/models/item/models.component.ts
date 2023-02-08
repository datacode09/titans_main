import {Component, OnInit, Input} from '@angular/core';


@Component({
  selector: 'modelsitem',
  templateUrl: './models.component.html',
  styleUrls: ['../../../app/app.component.css', './models.component.css']
})
export class ModelsItemComponent implements OnInit {
  
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



