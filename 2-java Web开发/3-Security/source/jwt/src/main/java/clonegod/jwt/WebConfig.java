package clonegod.jwt;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WebConfig {
	@Bean
    public FilterRegistrationBean demoFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new JwtFilter());
        registration.addUrlPatterns("/api/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("jwtFilter");

        return registration;
    } 
}
