import {Component, OnInit} from '@angular/core';
import {MapManager} from '../../MapManager';
import {ProjectExtentLayer} from './layers/ProjectExtentLayer';
import {ProjectService} from '../../ProjectService';
import {Project} from '../Project';
import {Geography} from '../Geography';
import {Objects} from 'src/core/types/Objects';
import {Subscription} from 'rxjs';
declare var dojo: any;
declare var esri: any;
declare var $: any;
@Component({
  selector: "projectsgeography-root"
  , templateUrl: "./projectsgeography.component.html"
  , styleUrls: ["projectsgeography.component.css"]

})
export class ProjectsGeographyComponent implements OnInit {
  public showmessage: boolean = false;
  private geographySubscriber: Subscription = null;
  public activatedraw: boolean = false;

  public constructor(private manager: MapManager, private service: ProjectService) {
  }

  /**
   * 
   */
  public ngOnInit(): void {
    this.service.getSelectedProject().subscribe(this.onSelectedProjectChanged.bind(this));
    var a = setInterval(() => {
      if (esri.Map) {
        clearInterval(a);
        this.initMap();
      }
    }, 10);
    this.manager.getActivateDrawGeography().subscribe(a => this.activatedraw = a);
  }


  /**
   * 
   */
  private onSelectedProjectChanged(p: Project): void {
    if (Objects.isNotNull(this.geographySubscriber)) {
      this.geographySubscriber.unsubscribe();
    }
    if (Objects.isNotNull(p)) {
      this.geographySubscriber = this.service.getProjectGeography(p.id).subscribe((g) => {
        this.updateMessageState(g);
        this.activatedraw = false;
      });
    }

  }


  /**
   * 
   */
  private updateMessageState(geography: Geography): void {
    this.showmessage = Objects.isNull(geography);
  }

  /**
   * 
   */
  private initMap() {
    const mapid = "projectgeography-map";
    dojo.ready(['dojo/domReady'], () => this.createMap(mapid));
    const layer = new ProjectExtentLayer(this.manager, this.service);
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
  public onMapReady(map: any): void {
    this.manager.setMap(map)
  }

  /**
   * 
   */
  public onActivate(): void {
    this.manager.setActivateDrawGeography(true);
  }

  /**
   * 
   */
  public onDeactivate(): void {
    this.manager.setActivateDrawGeography(false);
  }

}
