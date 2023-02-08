import {DetailsView} from './DetailsView';

import {RasterEntityPropertyDetailsView} from './RasterEntityPropertyDetailsView';
import {RasterGroupPropertyDetailsView} from './RasterGroupPropertyDetailsView';
import {RasterParameterPropertyDetailsView} from './RasterParameterPropertyDetailsView';
import {NullDetailsView} from './NullDetailsView';
import {RasterEntity} from 'src/core/rasters/RasterEntity';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {Injectable} from '@angular/core';
import {RastersService} from 'src/services/rasterservices/RastersService';
import {CombinedDetailsView} from './CombinedDetailsView';

/**
 * 
 */
@Injectable({
  providedIn: 'root'
})
export class PropertyDetailsViewFactory {

  public constructor(private service: RastersService) {
  }

  /**
   * 
   */
  public get(a: any): DetailsView {
    let result: DetailsView;
    if (a instanceof RasterEntity) {
      result = new RasterEntityPropertyDetailsView(a);
    } else if (a instanceof RastersGroup) {
      result = new RasterGroupPropertyDetailsView(a);
    } else if (a instanceof RasterParameter) {
      result = this.getRasterParameterPropertyDetailsView(a);
    } else {
      result = new NullDetailsView();
    }
    return result;
  }

  /**
   * 
   */
  private getRasterParameterPropertyDetailsView(a: RasterParameter): DetailsView {
    const result = new RasterParameterPropertyDetailsView(a);
    const gr = this.service.getRasterGroup(a.rasterId);
    const b = this.get(gr);
    const el = result.getElement().prepend(b.getElement());
    const combined = new CombinedDetailsView({
      title: result.getTitle.bind(result),
      element: ()=>el
    });
    return combined;
  }
}
