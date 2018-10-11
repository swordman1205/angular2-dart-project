package com.qurasense.userApi.itest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration(exclude = {/*SecurityAutoConfiguration.class*/
        /*, OAuth2ServerConfiguration.AuthorizationServerConfiguration.class*/})
@ComponentScan(basePackages = {"com.qurasense"}, excludeFilters=
        {@ComponentScan.Filter(type= FilterType.REGEX, pattern = "com.qurasense.userApi.repository.*")/*,
         @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = OAuth2ServerConfiguration.ResourceServerConfiguration.class)*/})
public class UserApiTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApiTestApplication.class, args);
    }


}
