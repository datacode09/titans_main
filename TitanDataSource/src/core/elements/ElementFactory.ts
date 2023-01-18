

/**
 * 
 */
export class ElementFactory {
  private constructor() {
  }

  /**
   * 
   */
  public static toJquery(settings: any): any {
    console.log(settings);
    const namediv = $("<span>")
      .css("margin-right: ", "10px")
      .css("display", "inline-block")
      .width("100px")
      .html(settings.name)
      ;
    const valuediv = $("<div>")
      .css("text-align", "right")
      .css("display", "inline-block")
      .html(settings.value);
    const result = $("<div>")
      .addClass("row")
      .css("font-size", "14px")
      .css("white-space", "nowrap")
      .css("margin-bottom", "10px")
      .append(namediv)
      .append(valuediv);

    return result;
  }
}

