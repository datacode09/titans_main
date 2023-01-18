
export class Objects {
    
  /**
   * 
   */
  private constructor() {
  }
  
  /**
   * 
   */
  public static isNull(obj:any):boolean {
    return obj === undefined && obj === null;
  }

  /**
   * 
   */
  public static isNotNull(obj:any):boolean {
    return !this.isNull(obj);
  }
}
