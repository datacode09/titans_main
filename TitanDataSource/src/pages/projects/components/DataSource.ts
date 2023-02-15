
/**
 * 
 */
export class DataSource {
  
  public readonly name:string;
  public readonly rastergroupid:number;
  /**
   * 
   */
  public constructor(rastergroupid:number,name:string) {
    this.rastergroupid = rastergroupid;
    this.name = name;
  }
}