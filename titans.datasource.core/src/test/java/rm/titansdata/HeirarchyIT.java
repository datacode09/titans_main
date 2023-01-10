package rm.titansdata;


import rm.titansdata.testdata.HeirarchyITTestData;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import rm.titansdata.heirarchy.Heirarchy;
import rm.titansdata.heirarchy.Node;

/**
 *
 * @author Ricardo Marquez
 */
public class HeirarchyIT {
  
  @Test
  public void getchildren() {
    Heirarchy parent = HeirarchyITTestData.getBasicHeirarchy(); 
    Set<Node> children = parent.getChildren(); 
    int expResult = 5;
    int result = children.size();
    Assert.assertEquals("getchildren", expResult, result);
  }
  
  @Test
  public void findchildbyname() {
    Heirarchy parent = HeirarchyITTestData.getBasicHeirarchy();
    String expResult = "a";
    Node child = parent.getChild(expResult);
    String result = child.getName();
    Assert.assertEquals("getchildren", expResult, result);
  }
  
}
