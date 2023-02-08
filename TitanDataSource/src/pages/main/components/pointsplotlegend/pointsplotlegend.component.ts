import {ViewEncapsulation, Component, OnInit} from '@angular/core';
import {AddPointManager} from 'src/services/addpoints/AddPointManager';
import {QueryPoint} from 'src/core/rasters/QueryPoint';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {RasterEntity} from 'src/core/rasters/RasterEntity';
import {PointAndRasterAssociations} from 'src/services/rasterassociations/PointAndRasterAssociations';
import {Subscription} from 'rxjs';


@Component({
  selector: 'pointsplotlegend',
  templateUrl: './pointsplotlegend.component.html',
  styleUrls: ['../../../../app/app.component.css', 'pointsplotlegend.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PointsPlotLegendComponent implements OnInit {

  public queryPoints: QueryPoint[] = [];

  private queryPointsSubscriptions: Map<number, Subscription> = new Map();

  /**
   * 
   */
  public constructor(private manager: AddPointManager,
    private associations: PointAndRasterAssociations,
    private service: RastersService) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    this.manager.getQueryPoints().subscribe(this.onQueryPointsChanged.bind(this));
    this.manager.getSelectedQueryPoint().subscribe(this.onQueryPointSelected.bind(this));
  }
  
  public onRowClicked(evt:any, queryPointId:number) {
    const arr = this.manager.getQueryPointsValue();
    const index = arr.findIndex(q => q.id === queryPointId);
    const queryPoint = arr[index]; 
    this.manager.setSelectedQueryPoint(queryPoint); 
  }
  
  /**
   * 
   */
  private onQueryPointSelected(queryPoint: QueryPoint): void {
    const list = $(".point-row");
    for (let i = 0; i < list.length; i++) {
      const d = $(list[i]);
      if (Number(d.parent().attr("id").replace("legenditem_", "")) === queryPoint.id) {
        $($(".point-row")[i]).addClass("selected");
      } else {
        $($(".point-row")[i]).removeClass("selected");
      }
    }
  }

  /**
   * 
   */
  private onQueryPointsChanged(queryPoints: QueryPoint[]): void {
    this.queryPoints = queryPoints;
    queryPoints.forEach(queryPoint => {
      const queryPointId = queryPoint.id;

      if (!this.queryPointsSubscriptions.has(queryPointId)) {
        const s = this.associations.getAssociations(queryPointId).subscribe(associations => {
          const parent = $("#legenditem_rasters_for_" + queryPointId).empty();
          associations.forEach(rasterid => {
            const div = $("<div>")
              .html("Raster = " + rasterid)
              .css("margin-left", 20)
            parent.append(div);
          })
        });
        this.queryPointsSubscriptions.set(queryPointId, s);
      }
    });
  }


  /**
   * 
   */
  public onAddButtonClicked(evt: any, queryId: number): void {
    const groups = this.service.getRastersValues();
    const popup = this.createPopup(queryId);
    groups.forEach(g => {
      const groupDiv = this.createGroupDiv(g);
      popup.append(groupDiv);
      g.rasterIds.forEach(rasterId => {
        const s = this.service.getRasterEntity(rasterId).subscribe((e) => {
          if (e != null) {
            const rasterDiv = this.createRasterEntityDiv(queryId, e);
            groupDiv.append(rasterDiv);
            setTimeout(() => {
              s.unsubscribe();
            });
          }
        });
      });
    });
  }

  /**
   * 
   */
  private createRasterEntityDiv(queryId: number, e: RasterEntity) {
    const checkbox = ($("<input>")
      .attr("type", "checkbox"))
      .attr("data-rasterId", e.rasterId)
      .css("display", "inline-block")
    checkbox.prop("checked", this.associations.hasAssociation(queryId, e.rasterId));
    const textdiv = ($("<div>")
      .css("display", "inline-block")
      .html("Raster : " + e.rasterId));
    const result = $("<div>")
      .css("margin-left", 20)
      .css("white-space", "nowrap")
      .append(checkbox)
      .append(textdiv);
    return result;
  }

  /**
   * 
   */
  private createGroupDiv(g: RastersGroup): any {
    const result = $("<div>")
      .append(($("<div>")
        .html(g.name)));
    return result;
  }

  /**
   * 
   */
  private createPopup(pointId: any): any {
    const popup = $("<div>");
    const header = this.createHeaderDiv(popup, pointId);
    const buttonRow = this.createButtonRowDiv(popup, pointId);
    const width = 500;
    const height = 400;
    const body = $("<div>")
      .attr("id", "associate_rasters_popup")
      .css("margin", "10px")

    popup.css("position", "absolute")
      .css("background-color", "white")
      .css("z-index", "100")

      .css("left", $(window).width() / 2 - width / 2)
      .css("top", $(window).height() / 4)
      .width(width + "px")
      .height(height + "px")
      .append(header)
      .append(body)
      .append(buttonRow)
      .show();
    $(document.body).append(popup);
    return body;
  }

  /**
   * 
   */
  private createButtonRowDiv(popup: any, pointId: number): any {

    const cancelbtn = $("<button>").html("Cancel")
      .click(evt => {
        popup.hide();
        popup.remove();
      });
    const savebtn = $("<button>")
      .css("margin-right", 5)
      .html("Save")
      .click(evt => {
        this.onSave(popup, pointId);
        popup.hide();
        popup.remove();
      });
    const result = $("<div>")
      .css("position", "absolute")
      .css("bottom", 0)
      .css("right", 0)
      .css("padding", 20)
      .css("text-align", "right")
      .append(savebtn)
      .append(cancelbtn);
    return result;
  }

  /**
   * 
   */
  private onSave(popup: any, pointId: number): void {
    const rasterIds: number[] = [];
    for (let i = 0; i < popup.find('input').length; i++) {
      const d = popup.find('input')[i];
      if ($(d).prop('checked')) {
        rasterIds.push(Number($(d).attr('data-rasterid')));
      }
    }
    this.associations.save(pointId, rasterIds);
  }

  /**
   * 
   */
  private createHeaderDiv(popup: any, pointId: number): any {
    const closeBtn = $("<div>")
      .addClass("closeBtn")
      .css("display", "inline-block")
      .html("x").click(e => {
        popup.hide();
        popup.remove();
      })
      ;

    const header = $("<div>")
      .css("background-color", "black")
      .css("white-space", "nowrap")
      .css("padding", "5px")
      .append(closeBtn)
      .append($("<div>")
        .css("display", "inline-block")
        .css("color", "white")
        .css("margin-left", "10px")
        .css("font-weight", "bold")
        .html("Select Rasters to Associate with Point " + pointId));
    ;
    return header;
  }
}