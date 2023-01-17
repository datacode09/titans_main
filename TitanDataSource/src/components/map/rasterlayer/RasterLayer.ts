import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {RasterImage} from 'src/core/rasters/RasterImage';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {RastersVisibilityService} from 'src/services/rasterstates/RastersVisibilityService';

declare var dojo: any;
declare var esri: any;
declare var $: any;

/**
 * //https://developers.arcgis.com/javascript/3/jsapi/mapimagelayer-amd.html#addimage
 */
export class RasterLayer {
  private map: any;
  private layers: Map<RasterParameter, any> = new Map();

  /**
   * 
   */
  public constructor(private service: RastersService, //
    private visibilityService: RastersVisibilityService) {
  }

  public onMapReady(map: any, param: RasterParameter): void {
    this.map = map;
    this.visibilityService.getVisibility(param).subscribe(visible => {
      if (visible && !this.layers.has(param)) {
        this.addLayer(param);
      }
      this.updateLayerVisibility(param);
    });
  }

  /**
   * 
   */
  private updateLayerVisibility(param: RasterParameter) {
    if (this.layers.has(param)) {
      const visible = this.visibilityService.getVisibility(param).value;
      const layer = this.layers.get(param);
      layer.setVisibility(visible);
    }
  }

  /**
   * 
   */
  private addLayer(param: RasterParameter): any {
    const options = {
      'id': this.toMapLayerId(param)
    };
    const layer = new esri.layers.MapImageLayer(options);
    this.map.addLayer(layer);
    const onReady = (image: any) => layer.addImage(image);
    this.createImage(param, onReady);
    this.layers.set(param, layer);
    this.updateLayerVisibility(param);
    return layer;
  }
  
  /**
   * 
   */
  private toMapLayerId(param: RasterParameter): string {
    return "customimage_" + param.rasterId + "_" + param.parameter.key;
  }


  /**
   * 
   */
  private createImage(param: RasterParameter, onReady: (img: any) => void): void {
    this.service.getMapImage(param).subscribe((img: RasterImage) => {
      if (img != null) {
        const imageURL = img.imageURL;
        const image = esri.layers.MapImage({
          'extent': {
            'xmin': img.envelope.xmin,
            'ymin': img.envelope.ymin,
            'xmax': img.envelope.xmax,
            'ymax': img.envelope.ymax,
            'spatialReference': {'wkid': img.envelope.spatialReference}
          },
          'href': imageURL
        });
        onReady(image);
      }
    });
  }
}
