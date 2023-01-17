

/**
 * 
 */
export class ElementFactory {
  
  private constructor() {
  }
  
  /**
   * 
   */
  public static toJquery(settings:any):any {
    console.log(settings); 
    return $("<div>").html("row");
  }
}

