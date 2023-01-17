import {DetailsView} from './DetailsView';
import {RasterEntity} from 'src/core/rasters/RasterEntity';

/**
 * 
 */
export class RasterEntityPropertyDetailsView implements DetailsView {

  private a: RasterEntity;

  public constructor(a: RasterEntity) {
    this.a = a;
  }
  /**
   * 
   */
  public getTitle(): string {
    const title = "Raster : " + (<RasterEntity> this.a).rasterId;
    return title;
  }

  /**
  * 
  */
  public getElement(): any {
    return $("<div>");
  }


}

