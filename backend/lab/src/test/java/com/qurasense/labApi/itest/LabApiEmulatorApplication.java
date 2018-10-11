package com.qurasense.labApi.itest;

import com.qurasense.labApi.LabApiApplication;
import com.qurasense.labApi.assay.repository.LocalLabRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration
@ComponentScan(
        basePackages = {"com.qurasense"},
        excludeFilters = {@ComponentScan.Filter(
            type=FilterType.ASSIGNABLE_TYPE,
            classes = {LocalLabRepository.class, LabApiTestApplication.class, LabApiApplication.class}
        )}
)
public class LabApiEmulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabApiEmulatorApplication.class, args);
    }

}
