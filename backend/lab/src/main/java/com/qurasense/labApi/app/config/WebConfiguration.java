package com.qurasense.labApi.app.config;

import java.util.List;

import com.qurasense.labApi.app.security.UserIdMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguration {

    @Autowired
    private UserIdMethodArgumentResolver userIdMethodArgumentResolver;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {

            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(userIdMethodArgumentResolver);
            }
        };
    }

//    @Bean
//    @Profile("emulator")
//    public FilterRegistrationBean requestDumperFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        Filter requestDumperFilter = new RequestDumperFilter();
//        registration.setFilter(requestDumperFilter);
//        registration.addUrlPatterns("/*");
//        return registration;
//    }
}
