

/**
 * 
 */
export class QueryPoint {
  private static ids = 0;
  
  public id:number;
  public mapPoint:any;
  public view:any;
    
  /**
   * 
   */
  public constructor(id:number, point:any, view:any) {
    this.id = id;
    this.mapPoint = point;
    this.view = view;
  }
  
  /**
   * 
   */
  public static create(point:any, view:any):QueryPoint {
    const id = QueryPoint.ids++;
    const instance = new QueryPoint(id, point, view);
    return instance;
  }
} 
