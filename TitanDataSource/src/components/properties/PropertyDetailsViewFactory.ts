import {DetailsView} from './DetailsView';

import {RasterEntityPropertyDetailsView} from './RasterEntityPropertyDetailsView';
import {RasterGroupPropertyDetailsView} from './RasterGroupPropertyDetailsView';
import {RasterParameterPropertyDetailsView} from './RasterParameterPropertyDetailsView';
import {NullDetailsView} from './NullDetailsView';
import {RasterEntity} from 'src/core/rasters/RasterEntity';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {RasterParameter} from 'src/core/rasters/RasterParameter';

/**
 * 
 */
export class PropertyDetailsViewFactory {

  /**
   * 
   */
  public static get(a: any): DetailsView {
    let result: DetailsView;
    if (a instanceof RasterEntity) {
      result = new RasterEntityPropertyDetailsView(a);
    } else if (a instanceof RastersGroup) {
      result = new RasterGroupPropertyDetailsView(a);
    } else if (a instanceof RasterParameter) {
      result = new RasterParameterPropertyDetailsView(a);
    } else {
      result = new NullDetailsView();
    }
    return result;
  }
}
