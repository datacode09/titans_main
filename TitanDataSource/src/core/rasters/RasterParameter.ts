
/**
 * 
 */
export class RasterParameter {
  
  public rasterId:number;
  public parameter:any;
  
  /**
   * 
   */
  public constructor(rasterId:number, parameter:any) {
    this.rasterId = rasterId;
    this.parameter = parameter;  
    if (typeof parameter.view === 'string') {
      this.parameter.view = JSON.parse(parameter.view); 
    }
  } 
  
}
