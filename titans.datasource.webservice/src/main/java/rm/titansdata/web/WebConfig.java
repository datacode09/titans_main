package rm.titansdata.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

//  @Bean
//  public MappedInterceptor myInterceptor(
//    @Autowired AuthenticationHandlerInterceptor bean) {
//    return new MappedInterceptor(null, bean);
//  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    WebMvcConfigurer.super.addCorsMappings(registry);  
    registry.addMapping("/**")   
      .allowCredentials(true)
//      .allowedOrigins(this.consumerUiOrigins)
      ;
  }
}
