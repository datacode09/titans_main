import {Component, OnInit, Input} from '@angular/core';
import {RastersService} from 'src/services/RastersService';
import {RastersGroup} from 'src/services/RastersGroup';
import {ViewEncapsulation} from '@angular/compiler/src/core';
import {RasterEntity} from 'src/services/RasterEntity';
import {RastersVisibilityService} from 'src/services/RastersVisibilityService';

@Component({
  selector: 'datasourcelist',
  templateUrl: './datasourcelist.component.html',
  styleUrls: ['../../app/app.component.css', './datasourcelist.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class DataSourceList implements OnInit {
  @Input()
  public title: string;
  public rastersgroup: RastersGroup[];

  public constructor(private service: RastersService,
    private visibilityService: RastersVisibilityService) {

  }


  /**
   * 
   */
  public ngOnInit(): void {
    this.service.getRasters().subscribe(e => {
      this.rastersgroup = e;
    });
  }
  /**
   * 
   */
  public onClicked(evt: any, name: string): void {
    const node: any = this.groupNameToEntity(name);
    this.expandNode(node);
  }

  /**
   * 
   */
  private groupNameToEntity(name: string): RastersGroup {
    const result = this.rastersgroup.find(g => g.name === name);
    return result;
  }

  /**
   * 
   */
  public expandNode(node: RastersGroup): void {
    node.rasterIds.forEach((rasterId) => {
      const subscription = this.service.getRasterEntity(rasterId);
      subscription.subscribe((raster) => {
        if (raster != null) {
          this.addNode(node, raster);
          subscription.unsubscribe();
        }
      });
    });
  }
  /**
   * 
   */
  public addNode(node: RastersGroup, raster: RasterEntity): void {
    const rasterId = raster.rasterId;
    const parentEl = $("#el_" + node.id);
    const newelement = "el_" + node.id + "_" + rasterId;
    if ($("#" + newelement).length == 0) {
      const text = $("<div>").text("Raster : " + rasterId + "")
        .css("display", "inline-block");
      const checkbox = $("<input type='checkbox'>")
        .css("display", "inline-block")
        ;
      checkbox.on("change", (evt)=>{
        const newvalue = checkbox.is(":checked");
        this.visibilityService.getVisibility(rasterId).next(newvalue);        
      }); 
      this.visibilityService.getVisibility(rasterId).subscribe((v)=>{
        checkbox.prop('checked', v);
      })
      $("<div>").attr("id", newelement)
        .append(checkbox)
        .append(text)
        .appendTo(parentEl)
        .css("whitespace", "none")
    } else {
      $("#" + newelement).toggle();
    }
  }

  /**
   * 
   */
  public onSelect(event: any): void {
    console.log(event);
    
  }
}

