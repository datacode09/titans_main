import {Component, OnInit} from '@angular/core';
declare var dojo: any;
declare var esri: any;
declare var $: any;
@Component({
  selector : "projectsgeography-root"
  , templateUrl : "./projectsgeography.component.html"
  , styleUrls : ["../../../../app/app.component.css", "projectsgeography.component.css"]
})
export class ProjectsGeographyComponent implements OnInit{
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
//    map.on("load", (evt: any) => this.onMapReady(map));
  }
  
    /**
   * 
   */
  private getBaseMap(): any {
    let result = "satellite";
    return result;
  }
}
