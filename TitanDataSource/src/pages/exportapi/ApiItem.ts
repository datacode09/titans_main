import {ApiParameter} from './ApiParameter';

/**
 * 
 */
export class ApiItem {
  public apipurpose: string
  public apicall: string;
  public parameters: ApiParameter[];
  public result: ApiParameter;
  
  /**
   * 
   */
  public constructor(arg: {
    apipurpose:string, parameters: ApiParameter[], apicall: string, result: ApiParameter}) {
    this.apipurpose = arg.apipurpose;
    this.parameters = arg.parameters;
    this.apicall = arg.apicall;
    this.result = arg.result;
  }

}

