package rm.titansdata.web.user.login;

/**
 *
 * @author Ricardo Marquez
 */
public class Credentials {

  public final String email;
  public final String password;
  
  /**
   * 
   * @param email
   * @param password 
   */
  public Credentials(String email, String password) {
    this.email = email;
    this.password = password;
  }

  /**
   * 
   * @return 
   */
  @Override
  public String toString() {
    return "{" + "email=" + email + ", password=" + password + '}';
  }
}
