/**
 * 
 */
export class RastersGroup {
  
  public id: number;
  public name: string;
  public rasterIds:number[];
  
  public constructor(id:number, name:string, rasterIds:number[]) {
    this.id = id;
    this.name = name;
    this.rasterIds = rasterIds;
  }
  
  /**
   * 
   */
  public contains(rasterId:number):boolean {
    const index = this.rasterIds.findIndex(v => v == rasterId);
    return index != -1;
  }
  
  /**
   * 
   */
  public size():number {
    return this.rasterIds.length;
  }
}

