import {Component, ViewEncapsulation, OnInit} from '@angular/core';
import {AddPointManager} from 'src/services/addpoints/AddPointManager';
import {QueryPoint} from 'src/core/rasters/QueryPoint';

@Component({
  selector: 'roiquerybar',
  templateUrl: './roiquerybar.component.html',
  styleUrls: ['../../app/app.component.css', './roiquerybar.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class RoiQueryBarComponent implements OnInit {

  public queryPointsArr: QueryPoint[] = [];
  public selectQueryPoint: QueryPoint = null;
  public constructor(private addPointManager: AddPointManager) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    this.addPointManager.getQueryPointActivated().subscribe(b => {
      if (b) {
        $("#query_point").addClass("activated");
      } else {
        $("#query_point").removeClass("activated");
      }
    });
    this.addPointManager.getQueryPoints().subscribe(this.onQueryPointsChanged.bind(this));
    this.addPointManager.getSelectedQueryPoint().subscribe((newvalue)=>{
      this.selectQueryPoint = newvalue;
    }); 
  }
  
  /**
   * 
   */
  public onQueryPointChange(event: any): void {
    const selectedId = Number($(event.srcElement).val()); 
    const index = this.queryPointsArr.findIndex(p => p.id === selectedId); 
    if (index != -1) {
      const newvalue = this.queryPointsArr[index]; 
      this.addPointManager.setSelectedQueryPoint(newvalue); 
    }
  }


  /**
   * 
   */
  private onQueryPointsChanged(d: QueryPoint[]): void {
    setTimeout(() => {
      if (d.length === 0) {
        $("#queryPointsList").hide();
      } else {
        $("#queryPointsList").show();
      }
      if (d.length > 0) {
        const end = d.length - 1;
        this.addPointManager.setSelectedQueryPoint(d[end]); 
      }
      this.queryPointsArr = d;
    });
  }

  /**
   * 
   */
  public onPointBtnClicked(event: any): void {
    this.addPointManager.toggleQueryPointActivated();
  }

}

