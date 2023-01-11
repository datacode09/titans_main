import {RastersService} from 'src/services/RastersService';
import {RastersVisibilityService} from 'src/services/RastersVisibilityService';
import {RasterImage} from 'src/services/RasterImage';

declare var dojo: any;
declare var esri: any;
declare var $: any;

/**
 * //https://developers.arcgis.com/javascript/3/jsapi/mapimagelayer-amd.html#addimage
 */
export class RasterLayer {
  private map: any;
  private layers: Map<number, any> = new Map();
  public constructor(private service: RastersService, //
    private visibilityService: RastersVisibilityService) {
  }

  public onMapReady(map: any, rasterId: number): void {
    this.map = map;
    this.visibilityService.getVisibility(rasterId).subscribe(v => {
      if (v && !this.layers.has(rasterId)) {
        this.addLayer(rasterId);
      }
      this.updateLayerVisibility(rasterId);
    });
  }

  private updateLayerVisibility(rasterId: number) {
    if (this.layers.has(rasterId)) {
      const visible = this.visibilityService.getVisibility(rasterId).value;
      const layer = this.layers.get(rasterId);
      layer.setVisibility(visible);
    }
  }

  /**
   * 
   */
  private addLayer(rasterId: number): any {
    const options = {
      'id': "customimage_" + rasterId
    };
    const layer = new esri.layers.MapImageLayer(options);
    this.map.addLayer(layer);
    const onReady = (image: any) => layer.addImage(image);
    this.createImage(rasterId, onReady);
    this.layers.set(rasterId, layer);
    this.updateLayerVisibility(rasterId);
    return layer;
  }


  /**
   * 
   */
  private createImage(rasterId: number, onReady: (img: any) => void): void {
    this.service.getMapImage(rasterId).subscribe((img: RasterImage) => {
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
