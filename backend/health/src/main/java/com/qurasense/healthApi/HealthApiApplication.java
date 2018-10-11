package com.qurasense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
//@EnableAutoConfiguration(exclude = org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class)
@ComponentScan(basePackages = {"com.qurasense"})
public class HealthApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthApiApplication.class, args);
    }
}
