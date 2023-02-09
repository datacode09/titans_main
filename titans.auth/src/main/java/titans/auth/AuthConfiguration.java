package titans.auth;

import common.db.DbConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class AuthConfiguration {

  /**
   *
   * @return
   */
  @Bean("auth.db")
  public DbConnection conn() {
    DbConnection result = new DbConnection.Builder()
      .setUrl("localhost")
      .setPort(5434)
      .setDatabaseName("titans.application")
      .setUser("postgres")
      .setUser("postgres")
      .setPassword("postgres")
      .createDbConnection();
    return result;
  }
}
