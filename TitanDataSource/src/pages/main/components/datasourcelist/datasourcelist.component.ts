import {Component, OnInit, Input} from '@angular/core';
import {ViewEncapsulation} from '@angular/compiler/src/core';
import {BehaviorSubject} from 'rxjs';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {RasterEntity} from 'src/core/rasters/RasterEntity';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {RastersVisibilityService} from 'src/services/rasterstates/RastersVisibilityService';
import {Objects} from 'src/core/types/Objects';

@Component({
  selector: 'datasourcelist'
  , templateUrl: './datasourcelist.component.html'
  , styleUrls: ['../../../../app/app.component.css', 'datasourcelist.component.css']
  , encapsulation: ViewEncapsulation.None
})
export class DataSourceList implements OnInit {
  @Input()
  public title: string;
  public rastersgroup: RastersGroup[];
  private selectedTreeNode: BehaviorSubject<any> = new BehaviorSubject(null);
  private elements: Map<String, any> = new Map();

  /**
   * 
   */
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
    this.selectedTreeNode.subscribe(this.selectTreeNodeBasedOnElement.bind(this));
    this.service.getSelectedItem().subscribe(this.onSelectedItemChanged.bind(this));
  }

  /**
   * 
   */
  private selectTreeNodeBasedOnElement(s: any): void {
    if (Objects.isNull(s) || !s.hasClass("selected")) {
      $(".node").removeClass("selected")
      if (Objects.isNotNull(s)) {
        s.addClass("selected");
        if (s.length > 0) {
          s[0].scrollIntoViewIfNeeded(true)
        }
        this.setSelectItem();
      }
    }
  }

  /**
   * 
   */
  private onSelectedItemChanged(item: any): void {
    if (item instanceof RasterParameter) {
      const id = this.getRasterParamNodeElementId(<RasterParameter> item);
      const element = $("#" + id);
      this.selectTreeNodeBasedOnElement(element);
    } else if (item instanceof RasterEntity) {
      const id = this.getRasterElementId((<RasterEntity> item).rasterId);
      const element = $("#" + id);
      this.selectTreeNodeBasedOnElement(element);
    }
  }

  /**
   * 
   */
  private setSelectItem(): void {
    const elId = $(".node.selected").attr("id");
    const element = this.elements.get(elId);
    this.service.setSelectedItem(element);
  }

  /**
   * 
   */
  public onClickedForSelection(evt: any, name: string) {
    const node: any = this.groupNameToEntity(name);
    const elementId = "el_" + node.id;
    const el = $("#" + elementId + "  .level01");
    const group = this.rastersgroup.find(g => g.id == node.id);
    this.elements.set(elementId, group);
    this.selectedTreeNode.next(el.parent());
    evt.preventDefault();
    evt.stopPropagation();
  }

  /**
   * 
   */
  public onClicked(evt: any, name: string): void {
    const node: any = this.groupNameToEntity(name);
    const el = $("#el_" + node.id + "  .level01");
    const visible: boolean = JSON.parse(el.attr("data-visible"));
    el.attr("data-visible", (!visible).toString());
    this.updateIcon(el);
    this.expandGroupNode(node);
    this.selectedTreeNode.next(el.parent());
    evt.preventDefault();
    evt.stopPropagation();
  }

  /**
   * 
   */
  private updateIcon(el: any): void {
    const visible: boolean = JSON.parse(el.attr("data-visible"));
    if (visible) {
      el.html("-");
    } else {
      el.html("+");
    }
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
  public expandGroupNode(node: RastersGroup): void {
    node.rasterIds.forEach((rasterId) => {
      this.loadRasterNode(node, rasterId);
    });
  }

  /**
   * 
   */
  private loadRasterNode(node: RastersGroup, rasterId: number): void {
    const subscription = this.service.getRasterEntity(rasterId);
    subscription.subscribe((raster) => {
      if (raster != null) {
        const rasternodeid = this.addRasterNode(node, raster);
        this.elements.set(rasternodeid, raster);
        this.service.getParameters(rasterId).subscribe(params => {
          params.forEach((param) => {
            const rasterParamNodeId = this.addRasterParamNode(param);
            this.elements.set(rasterParamNodeId, param);
          });

        });
        subscription.unsubscribe();
      }
    });
  }

  /**
   * 
   */
  private addRasterNode(node: RastersGroup, raster: RasterEntity): any {
    const rasterId = raster.rasterId;
    const parentEl = $("#el_" + node.id);
    const newelement = this.getRasterElementId(rasterId);
    if ($("#" + newelement).length == 0) {
      const textdiv = $("<div>").text("Raster : " + rasterId + "")
        .css("display", "inline-block")
        .addClass("rasterdiv")
        ;
      const c = $("<div>").attr("id", newelement)
        .addClass("node")
        .append(textdiv)
        .append($("<div>").addClass("rasterelements"))
        .appendTo(parentEl)
        .click((e: any) => {
          e.preventDefault();
          e.stopPropagation();
          this.selectedTreeNode.next(c);
        })
        .css("whitespace", "none")
    } else {
      $("#" + newelement).toggle();
    }
    return newelement;
  }

  /**
   * 
   */
  private getRasterElementId(rasterId: number): string {
    const index = this.rastersgroup.findIndex((g) => g.contains(rasterId));
    const g = this.rastersgroup[index];
    return "el_" + g.id + "_" + rasterId;
  }


  /**
   * 
   */
  private addRasterParamNode(param: RasterParameter): string {
    const id = this.getRasterParamNodeElementId(param);
    if ($("#" + id).length > 0) {
      return;
    }
    const rasterId = param.rasterId;
    const parentEl = $("#" + this.getRasterElementId(rasterId) + " .rasterelements");
    const checkbox = $("<input type='checkbox'>")
      .css("display", "inline-block");
    checkbox.on("click", (evt) => {
      const newvalue = checkbox.is(":checked");
      this.visibilityService.getVisibility(param).next(newvalue);
    });
    this.visibilityService.getVisibility(param).subscribe((v) => {
      setTimeout(() => {
        const a: any = checkbox[0];
        a.checked = v;
      })

    });
    const key = param.parameter.key;
    const textdiv = $("<span>").html(key);
    const c = $("<div>").attr("id", id)
      .addClass("parameterdiv")
      .addClass("node")
      .append(checkbox)
      .append(textdiv)
      .on("click", (e) => {
        e.preventDefault();
        e.stopPropagation();
        this.selectedTreeNode.next(c);
      })
      .appendTo(parentEl);

    return id;
  }

  /**
   * 
   */
  private getRasterParamNodeElementId(param: RasterParameter): string {
    const rasterId = param.rasterId;
    const key = param.parameter.key;
    const id = this.getRasterElementId(rasterId) + "_" + key;
    return id;
  }


  /**
   * 
   */
  public onSelect(event: any): void {
    console.log(event);
  }
}

