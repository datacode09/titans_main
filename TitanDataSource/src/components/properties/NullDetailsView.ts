import {DetailsView} from './DetailsView';

/**
 * 
 */
export class NullDetailsView implements DetailsView {
  public getTitle(): string {
    return "N/A";
  }
  
  /**
   * 
   */
  public getElement():any {
    const result = $("<div>"); 
    return result;
  }
}
