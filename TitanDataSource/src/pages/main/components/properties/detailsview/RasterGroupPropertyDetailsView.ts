import {DetailsView} from './DetailsView';
import {RastersGroup} from 'src/core/rasters/RastersGroup';
import {ElementFactory} from 'src/core/elements/ElementFactory';

/**
 * 
 */
export class RasterGroupPropertyDetailsView implements DetailsView {
  private a: any;

  public constructor(a: any) {
    this.a = a;
  }
  
  /**
   * 
   */
  public getTitle(): string {
    const title = (<RastersGroup> this.a).name;
    return title;
  }

  /**
    * 
    */
  public getElement(): any {
    const result = ElementFactory.toJquery({
      name : "Group:", value : (<RastersGroup> this.a).name
    }); 
    return result;
  }


}

