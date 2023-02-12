import {Component, OnInit} from '@angular/core';
import {MapManager} from '../../MapManager';
import {ProjectExtentLayer} from './layers/ProjectExtentLayer';
declare var dojo: any;
declare var esri: any;
declare var $: any;
@Component({
  selector: "projectsgeography-root"
  , templateUrl: "./projectsgeography.component.html"
  , styleUrls: ["projectsgeography.component.css"]
  
})
export class ProjectsGeographyComponent implements OnInit {
  
  public constructor(private manager:MapManager) {
  }
  
  /**
   * 
   */
  public ngOnInit(): void {
    var a = setInterval(() => {
      if (esri.Map) {
        clearInterval(a);
        this.initMap();
      }
    }, 10);
  }
  /**
   * 
   */
  private initMap() {
    const mapid = "projectgeography-map";
    dojo.ready(['dojo/domReady'], () => this.createMap(mapid));
    const layer = new ProjectExtentLayer(this.manager);
    layer.init();
  }

  /**
 * 
 */
  private createMap(mapid: string): void {
    const map = esri.Map(mapid, {
      basemap: this.getBaseMap()
      , zoom: 8
      , center: [-120.43, 37.36]
      , height: "100%"
    });
    map.on("load", (evt: any) => this.onMapReady(map));
  }

  /**
   * 
   */
  private getBaseMap(): any {
    let result = "satellite";
    return result;
  }
  
  /**
   * 
   */
  public onMapReady(map:any):void {
    this.manager.setMap(map)
  }
}
