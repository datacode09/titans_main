package rm.titansdata.web;

import common.db.DbConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
public class TitansDataConfiguration {
  
  @Bean("titans.db")
  public DbConnection titansdbconn() {
    DbConnection result = new DbConnection.Builder()
      .setUrl("localhost")
      .setPort(5434)
      .setDatabaseName("titans.application")
      .setUser("postgres")
      .setPassword("postgres")
      .createDbConnection();
    return result;
  }
}
