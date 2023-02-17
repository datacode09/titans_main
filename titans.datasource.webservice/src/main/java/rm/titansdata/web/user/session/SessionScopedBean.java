package rm.titansdata.web.user.session;

import java.util.Objects;
import java.util.function.Supplier;

/**
 *
 * @author Ricardo Marquez
 */
public class SessionScopedBean<T> {

  private final SessionManager m;
  private Supplier<T> supplier;

  public SessionScopedBean(SessionManager m, Supplier<T> supplier) {
    this.m = m;
    this.supplier = supplier;
  }

  /**
   *
   * @param auth
   * @return
   */
  public T getValue(String auth) {
    Objects.requireNonNull(auth, "Auth token is undefined");
    T result;
    if (this.m.existsByToken(auth)) {
      result = this.supplier.get();
    } else {
      throw new RuntimeException("Invalid auth token");
    }
    return result;
  }

  /**
   * 
   * @param authToken
   * @param p 
   */
  public void setValue(String authToken, T p) {
    if (this.m.existsByToken(authToken)) {
      this.supplier = () -> p;
    } else { 
      throw new RuntimeException(
        String.format("Invalid token '%s'", authToken));   
    }
  }
}
