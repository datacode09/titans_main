import {ApiParameter} from './ApiParameter';

/**
 * 
 */
export class ApiItem {
  public parameters: ApiParameter[];
  public apicall: string;
  public result: ApiParameter;
  
  /**
   * 
   */
  public constructor(arg: {parameters: ApiParameter[], apicall: string, result: ApiParameter}) {
    this.parameters = arg.parameters;
    this.apicall = arg.apicall;
    this.result = arg.result;
  }

}

