import {DetailsView} from './DetailsView';
import {QueryPoint} from '../map/geometries/QueryPoint';
import {Objects} from 'src/core/types/Objects';
import {ElementFactory} from 'src/core/elements/ElementFactory';

/**
 * 
 */
export class QueryPointPropertyDetailsView implements DetailsView {

  private a: QueryPoint;

  /**
   * 
   */
  public constructor(a: QueryPoint) {
    this.a = a;
  }

  /**
   * 
   */
  public getTitle(): string {
    return "Point " + this.a.id;
  }

  /**
   * 
   */
  public getElement(): any {
    const result = $("<div>");
    this.addProperties(result);
    return result;
  } 
  
  /**
   * 
   */
  private addProperties(result: any):void {
    if (Objects.isNotNull(this.a.view)) {
      const view = this.a.view;
      view.forEach((setting: any) => {
        const element: any = ElementFactory.toJquery(setting);
        result.append(element);
      });
    }
  }
}