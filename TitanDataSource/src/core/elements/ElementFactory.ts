

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
    const namediv = $("<span>")
      .css("margin-right: ", "10px")
      .css("font-weight", "bold")
      .width("100px")
      .html(settings.name)
      ;
    const valuediv = $("<div>")
      .css("text-align", "left")
      .css("word-break", "break-word")
      .css("width", "calc(100% - 100px")
      .css("display", "inline-block")
      .html(settings.value);
    const result = $("<div>")
      .addClass("row")
      .css("font-size", "14px")
      .css("margin-bottom", "10px")
      .append(namediv)
      .append(valuediv);

    return result;
  }
}

