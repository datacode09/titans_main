import {OnInit, ViewEncapsulation, Component} from '@angular/core';
import {RasterLayer} from './rasterlayer/RasterLayer';
import {RastersService} from 'src/services/RastersService';
import {RastersVisibilityService} from 'src/services/RastersVisibilityService';

declare var dojo: any;
declare var esri: any;
declare var $: any;


@Component({
  selector: 'map',
  templateUrl: './map.component.html',
  styleUrls: ['../../app/app.component.css', './map.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class MapComponent implements OnInit {
  
  
  public constructor(private service:RastersService, 
    private visibilityService: RastersVisibilityService) {
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
    const mapid = "map";
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
    map.on("load", (evt: any) => this.onMapReady(map));
  }
  
  /**
   * 
   */
  private onMapReady(map: any): void {
    const layer = new RasterLayer(this.service, this.visibilityService);
    this.service.getRasters().subscribe(groups=>{
      groups.forEach(g=>{
        g.rasterIds.forEach(rasterId=>{
          layer.onMapReady(map, rasterId);
        }); 
      });
    });  
  }
  
  /**
   * 
   */
  private getBaseMap(): any {
    let result = "satellite";
    return result;
  }
}

