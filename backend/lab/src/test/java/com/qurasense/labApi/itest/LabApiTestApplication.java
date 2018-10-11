package com.qurasense.labApi.itest;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.datastore.UUIDIdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.local.LocalEntityRepository;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.qurasense.labApi.assay"}, excludeFilters=
        {@ComponentScan.Filter(type= FilterType.REGEX, pattern = "com.qurasense.healthApi.repository.google.*")})
public class LabApiTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabApiTestApplication.class, args);
    }

    @Bean
    public EventStorageEngine eventStorageEngine() {
        return new InMemoryEventStorageEngine();
    }

    @Bean
    public EntityRepository entityRepository() {
        return new LocalEntityRepository();
    }

    @Bean
    public IdGenerationStrategy idGenerationStrategy() {
        return new UUIDIdGenerationStrategy();
    }

}
