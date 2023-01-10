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
  
  public size():number {
    return this.rasterIds.length;
  }
}

