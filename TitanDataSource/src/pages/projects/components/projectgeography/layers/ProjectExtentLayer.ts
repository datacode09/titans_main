import {MapManager} from 'src/pages/projects/MapManager';

declare var dojo:any;
declare var esri:any;
declare var $:any;

export class ProjectExtentLayer {
  private toolbar:any = null;
  
  /**
   * 
   */
  public constructor(private manager:MapManager) {
  }
  
  /**
   * 
   */
  public init() {
    dojo.require('esri/toolbars/draw');
    setTimeout(()=>{
      
      this.manager.getMap().subscribe(this.onMapChanged.bind(this)); 
    }); 
  }
  
  /**
   * 
   */
  private onMapChanged(map:any):void {
    if (map !== null) {
      this.initMapLayer(map); 
    }
  }
  
  /**
   * 
   */
  private initMapLayer(map:any): void {
    this.toolbar = new esri.toolbars.Draw(map);
    this.toolbar.activate(esri.toolbars.Draw.EXTENT); 
  }
}

