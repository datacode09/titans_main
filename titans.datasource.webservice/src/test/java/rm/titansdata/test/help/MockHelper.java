package rm.titansdata.test.help;

import org.json.JSONObject;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 *
 * @author Ricardo Marquez
 */
public class MockHelper {

  private final MockHttpServletRequestBuilder builder;
  private final MockMvc mockMvc;

  public MockHelper(MockMvc mockMvc, String path) {
    this.builder = MockMvcRequestBuilders.get(path);
    this.mockMvc = mockMvc;
  }
  
  /**
   * 
   * @param mockMvc
   * @param path
   * @return 
   */
  public static MockHelper asGet(MockMvc mockMvc, String path) {
    MockHelper instance = new MockHelper(mockMvc, MockMvcRequestBuilders.get(path)); 
    return instance;
  }
  
  public static MockHelper asPost(MockMvc mockMvc, String path) {
    MockHelper instance = new MockHelper(mockMvc, MockMvcRequestBuilders.post(path)); 
    return instance;
  }
  
  /**
   * 
   * @param mockMvc
   * @param builder 
   */
  private MockHelper(MockMvc mockMvc, MockHttpServletRequestBuilder builder) {
    this.mockMvc = mockMvc;
    this.builder = builder;
  }
  
  
  
  /**
   * 
   * @param key
   * @param value
   * @return 
   */
  public MockHelper setParam(String key, String value) {
    this.builder.param(key, value);
    return this;
  }
  
  /**
   * 
   * @return 
   */
  public JSONObject perform() {
    JSONObject jsonObj;
    try {
      MvcResult result = this.mockMvc
        .perform(builder)
        .andReturn();
      MockHttpServletResponse response = result.getResponse();
      String text = java.net.URLDecoder.decode(
        response.getContentAsString(), response.getCharacterEncoding());
      jsonObj = new JSONObject(text);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return jsonObj;
  }

}
