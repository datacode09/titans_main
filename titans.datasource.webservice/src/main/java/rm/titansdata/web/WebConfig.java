package rm.titansdata.web;

import org.springframework.beans.factory.annotation.Autowired;
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
  
//  @Autowired()
//  @Qualifier("consumerUiOrigins")
//  private String[] consumerUiOrigins;

  @Bean
  public MappedInterceptor myInterceptor(  
    @Autowired AuthenticationHandlerInterceptor bean) {
    return new MappedInterceptor(null, bean);    
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    WebMvcConfigurer.super.addCorsMappings(registry);  
    registry.addMapping("/**")     
      .allowCredentials(true)    
      .allowedOrigins("http://localhost:4200")
      .allowedOrigins("http://localhost:8081/titans.datasource")
//      .allowedOrigins(this.consumerUiOrigins)
      ;
  }
}
