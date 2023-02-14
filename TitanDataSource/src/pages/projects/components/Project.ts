
/**
 * 
 */
export class Project {
  
  public id:number; 
  public title: string;
  
  public constructor(id:number, title:string) {
    this.id = id;
    this.title = title;
  }
    
  /**
   * 
   */
  public static createInstance(): Project{
    const id = Math.round(Math.random()*1000);
    const title = "New Project (" + id + ")";
    const instance = new Project(id, title);
    return instance ; 
  }
}

