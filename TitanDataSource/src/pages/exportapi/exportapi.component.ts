import {Component, OnInit} from '@angular/core';
import {ApiItem} from './ApiItem';
import {ApiParameter} from './ApiParameter';

@Component({
  selector: 'exportapi'
  , templateUrl: './exportapi.component.html'
  , styleUrls: ['./exportapi.component.css']  
})
export class ExportApiComponent implements OnInit{
  public items:ApiItem[] = [
    new ApiItem({
      apipurpose : "Raster Series"
      , parameters: [new ApiParameter("userId", "long"), new ApiParameter("rasterId", "long")]
      , apicall : "https://epstechnology.com/datasource/apis/getRaster?userId=<userId>&rasterId=<rasterId>"
      , result: new ApiParameter("values", "{validtime:string, value:number}[]")
    }), 
    new ApiItem({
      apipurpose : "Raster Images"
      , parameters: [new ApiParameter("rasterId", "long")]
      , apicall : "https://epstechnology.com/datasource/apis/getRaster?rasterId=<rasterId>"
      , result: new ApiParameter("values", "{validtime:string, value:number}[]")
    })
  ];
  
  /**
   * 
   */
  public ngOnInit(): void {
    
  }  
}

