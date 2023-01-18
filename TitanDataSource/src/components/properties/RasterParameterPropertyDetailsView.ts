import {DetailsView} from './DetailsView';
import {ElementFactory} from 'src/core/elements/ElementFactory';
import {RasterParameter} from 'src/core/rasters/RasterParameter';
import {Objects} from 'src/core/types/Objects';

export class RasterParameterPropertyDetailsView implements DetailsView {
  private a: RasterParameter;
  
  /**
   * 
   */
  public constructor(a: any) {
    this.a = <RasterParameter> a;
  }

  /**
   * 
   */
  public getTitle(): string {
    const result = this.a.rasterId
      + " : " + this.a.parameter.key;
    return result;
  }

  /**
    * 
    */
  public getElement(): any {
    const parameter = this.a.parameter;
    const result = $("<div>");
    console.log(parameter); 
    this.addProperties(result, parameter);
    result;
    return result;
  }

  private addProperties(result: any, parameter: any): void {
    if (Objects.isNotNull(parameter.view)) {
      const view = parameter.view;
      console.log(parameter); 
      view.forEach((setting: any) => {
        const element: any = ElementFactory.toJquery(setting);
        result.append(element);
      });
    }
  }
}

