package rm.titansdata.heirarchy;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ricardo Marquez
 */
public class Heirarchy {

  private final Set<Node> nodes = new HashSet<>();
  private final String name;
  
  /**
   * 
   * @param name
   * @param nodes 
   */
  public Heirarchy(String name, Set<Node> nodes) {
    this.nodes.addAll(nodes);
    this.name =name;
  }
  
  /**
   * 
   * @return 
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @return
   */
  public Set<Node> getChildren() {
    return nodes;
  }

  /**
   * Finds the child based on name. If not child is found, then null is returned.
   *
   * @param name
   * @return 
   */
  public Node getChild(String name) {
    Node result = this.nodes.stream().filter(n -> n.getName().equals(name))
      .findFirst()
      .orElse(null);
    return result;
  }
}
