package rm.titansdata.web.user.session;

import java.util.function.Supplier;

/**
 *
 * @author Ricardo Marquez
 */
public class SessionScopedBean<T> {

  private final SessionManager m;
  private final Supplier<T> supplier;

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
    T result;
    if (this.m.existsByToken(auth)) {
      result = this.supplier.get();
    } else {
      result = null;
    }
    return result;
  }
}
