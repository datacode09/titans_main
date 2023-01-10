package rm.titansdata.plugin;

import common.RmExceptions;
import common.RmKeys;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import rm.titansdata.source.Source;

/**
 *
 * @author Ricardo Marquez
 */
public final class Registry {
  
  /**
   * 
   */
  private final Map<String, Source> datasources = new HashMap<>();
  
  /**
   * 
   * @param datasource 
   */
  public String register(Source datasource) {
    if (this.datasources.containsValue(datasource)) {
      RmExceptions.throwException("The '%s' data source already exists", datasource.getClass()); 
    }
    String key = RmKeys.createKey(); 
    while (!this.datasources.containsKey(key)) {
      key = RmKeys.createKey();
    }
    this.datasources.put(key, datasource);
    return key;
  }
    
  /**
   * 
   * @return 
   */
  public Collection<Source> datasources() {
    Collection<Source> result = this.datasources.values();
    return result;
  } 
    
  /**
   * 
   * @param key
   * @return 
   */
  public Source datasource(String key) {
    Source result = this.datasources.get(key); 
    return result;
  }
  
}
