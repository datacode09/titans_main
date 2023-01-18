

/**
 * 
 */
export class QueryPoint {
  private static ids = 0;
  
  public id:number;
  public mapPoint:any;
    
  /**
   * 
   */
  public constructor(id:number, point:any) {
    this.id = id;
    this.mapPoint = point;
  }
  
  /**
   * 
   */
  public static create(point:any):QueryPoint {
    const id = QueryPoint.ids++;
    const instance = new QueryPoint(id, point);
    return instance;
  }
} 
