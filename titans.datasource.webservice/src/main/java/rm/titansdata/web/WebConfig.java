package rm.titansdata.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.MappedInterceptor;
import rm.titansdata.web.user.session.AuthenticationHandlerInterceptor;

/**
 *
 * @author Ricardo Marquez
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
  
  @Autowired()
  @Qualifier("cors-list")    
  private String consumerUiOrigins;  

  @Bean
  public MappedInterceptor myInterceptor(    
    @Autowired AuthenticationHandlerInterceptor bean) {
    return new MappedInterceptor(null, bean);    
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    System.out.println("cors"  + this.consumerUiOrigins);
    WebMvcConfigurer.super.addCorsMappings(registry);  
    registry.addMapping("/**")     
      .allowCredentials(true)    
      .allowedOrigins(this.consumerUiOrigins.split(","))
//      .allowedOrigins(this.consumerUiOrigins)
      ;
  }
}
