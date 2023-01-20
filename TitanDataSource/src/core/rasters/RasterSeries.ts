
/**
 * 
 */
export class RasterSeries {

  /**
   * 
   */
  public constructor(public rasterId:number, private data: any[]) {

  }

  /**
   * 
   */
  public toData(): any[] {
    return this.data;
  }
}