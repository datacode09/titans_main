import {Project} from './components/Project';


export class ProjectEntity {
  public readonly projectId: number;
  public readonly name: string;
  public readonly lowerleft: any;
  public readonly upperright: any;
  public readonly srid: number;
  public readonly rastergroupIds: number[];

  public constructor(arg: {
    projectId: number,
    name: string,
    lowerleft: any,
    upperright: any,
    srid: number,
    rastergroupIds: number[]
  }) {
    this.projectId = arg.projectId;
    this.name = arg.name;
    this.lowerleft = arg.lowerleft;
    this.upperright = arg.upperright;
    this.srid = arg.srid;
    this.rastergroupIds = arg.rastergroupIds;
  }
    
  /**
   * 
   */
  public toProject():Project {
    const result = new Project(this.projectId, this.name);
    return result;
  }
}
