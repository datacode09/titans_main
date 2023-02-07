import {DetailsView} from './DetailsView';

export class CombinedDetailsView implements DetailsView {
  private title: () => {};
  private element: () => {};
  /**
   * 
   */
  public constructor(arg: {title: () => {}, element: () => {}}) {
    this.title = arg.title;
    this.element = arg.element;
  }
  
  /**
   * 
   */
  public getTitle(): string {
    return <string> this.title();
  } 
  
  /**
   * 
   */  
  public getElement() {
    return this.element();
  }

}
