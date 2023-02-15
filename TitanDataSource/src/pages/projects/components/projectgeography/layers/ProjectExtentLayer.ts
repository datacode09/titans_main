import {MapManager} from 'src/pages/projects/MapManager';
import {ProjectService} from 'src/pages/projects/ProjectService';
import {Project} from '../../Project';
import {Subscription, BehaviorSubject} from 'rxjs';
import {Objects} from 'src/core/types/Objects';
import {Geography} from '../../Geography';
import {Envelope} from 'src/core/types/Envelope';

declare var dojo: any;
declare var esri: any;
declare var $: any;

export class ProjectExtentLayer {

  private toolbar: any = null;
  private geographySubscriber: Subscription = null;
  private activeToolbar: BehaviorSubject<boolean> = new BehaviorSubject(false);
  private layer: any = null;

  /**
   * 
   */
  public constructor(private manager: MapManager, private service: ProjectService) {
  }

  /**
   * 
   */
  public init() {
    this.manager.getMap().subscribe(this.onMapChanged.bind(this));
    this.service.getSelectedProject().subscribe(this.onSelectedProjectChanged.bind(this));
    this.activeToolbar.subscribe(this.updateToolBar.bind(this));
    this.manager.getActivateDrawGeography().subscribe(this.updateToolBar.bind(this));
  }

  /**
   * 
   */
  private updateToolBar(a: boolean): void {
    if (this.toolbar != null) {
      if (a) {
        this.toolbar.activate(esri.toolbars.Draw.EXTENT);
      } else {
        this.toolbar.activate(esri.toolbars.Draw.EXTENT);
        this.toolbar.deactivate();
      }
    }

  }

  /**
   * 
   */
  public onSelectedProjectChanged(p: Project): void {
    if (Objects.isNotNull(this.geographySubscriber)) {
      this.geographySubscriber.unsubscribe();
    }
    if (Objects.isNotNull(p)) {
      const projGeography = this.service.getProjectGeography(p.id);

      this.geographySubscriber
        = projGeography.subscribe(this.onProjectGeographyChanged.bind(this));
    }
  }

  private onProjectGeographyChanged(g: Geography): void {
    this.updateActiveState(g);
    this.updateMapLayer(g);
  }

  /**
   * 
   */
  private updateMapLayer(g: Geography): void {
    this.layer.clear();
    if (g != null) {
      console.log(g);
      const extent = esri.geometry.Extent(g.toMapGeometry());
      const graphicSymbol = esri.symbol.SimpleFillSymbol();
      const graphic = new esri.Graphic(extent, graphicSymbol);
      this.layer.add(graphic);
      this.layer.getMap().centerAt(extent.getCenter());
    }
  }

  /**
   * 
   */
  private updateActiveState(g: Geography): void {
    this.activeToolbar.next(this.manager.getActivateDrawGeographyValue());
  }

  /**
   * 
   */
  private onMapChanged(map: any): void {
    if (map !== null) {
      const self = this;
      dojo.require('esri/toolbars/draw');
      dojo.addOnLoad(() => {
        self.initToolBar(map);
      });
      self.initMapLayer(map);
    }
  }

  /**
   * 
   */
  private onDrawComplete(evt: any): void {
    console.log(evt.geometry);
    this.setGeographyFromDrawEvent(evt);
    this.manager.setActivateDrawGeography(false);
  }

  /**
   * 
   */
  private setGeographyFromDrawEvent(evt: any): void {
    const xmin = evt.geometry.xmin; 
    const xmax = evt.geometry.xmax; 
    const ymin = evt.geometry.ymin; 
    const ymax = evt.geometry.ymax; 
    const srid = 3857;
    const env = new Envelope(xmin, xmax, ymin, ymax, srid);
    const geography = new Geography({envelope:env});
    const projectId = this.service.getSelectedProjectValue().id;
    this.service.setGeography(projectId, geography);

  }

  /**
   * 
   */
  private initMapLayer(map: any): void {
    this.layer = new esri.layers.GraphicsLayer();
    map.addLayer(this.layer);
  }

  /**
   * 
   */
  private initToolBar(map: any): void {
    this.toolbar = new esri.toolbars.Draw(map);
    this.toolbar.on("draw-end", this.onDrawComplete.bind(this));
    this.updateToolBar(this.manager.getActivateDrawGeographyValue());
  }
}

