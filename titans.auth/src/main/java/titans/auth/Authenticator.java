package titans.auth;

import common.RmKeys;
import common.db.DbConnection;
import common.db.RmDbUtils;
import java.sql.ResultSet;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class Authenticator {

  private final DbConnection conn;

  public Authenticator(@Qualifier("auth.db") DbConnection conn) {
    this.conn = conn;
  }

  /**
   *
   * @param email
   * @param password
   */
  public Optional<String> authenticate(String email, String password) {
    String query = this.getAuthQuery(email, password);
    Boolean valid = this.conn.executeQuerySingleResult(query, this::isValid);
    Optional<String> result = (valid == null || !(valid))
      ? Optional.empty()
      : Optional.of(RmKeys.createKey());
    return result;
  }

  /**
   *
   * @param rs
   * @return
   */
  private boolean isValid(ResultSet rs) {
    boolean result = RmDbUtils.booleanValue(rs, "isvalid");
    return result;
  }

  /**
   *
   * @param email
   * @param password
   * @return
   */
  private String getAuthQuery(String email, String password) {
    String result = "select\n"
      + String.format("	a.key = '%s' as isvalid \n", password)
      + "from users.\"user\" u\n"
      + "join users.authentication a\n"
      + "on a.user_id = u.user_id\n"
      + String.format("where u.email = '%s'", email);
    return result;
  }

}
