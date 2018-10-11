package com.qurasense.healthApi;

import com.google.cloud.datastore.Datastore;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.local.LocalEntityRepository;
import com.qurasense.healthApi.sample.storage.StorageService;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.qurasense"}, excludeFilters=
        {@ComponentScan.Filter(type= FilterType.REGEX, pattern = "com.qurasense.healthApi.repository.google.*")/*,
         @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes = OAuth2ServerConfiguration.ResourceServerConfiguration.class)*/})
public class HealthApiTestApplication {

    @MockBean
    private StorageService storageService;

    @MockBean
    private Datastore datastore;

    public static void main(String[] args) {
        SpringApplication.run(HealthApiTestApplication.class, args);
    }

    @Bean
    public EntityRepository simpleRepository() {
        return new LocalEntityRepository();
    }

    @Bean("broadcastMessagePublisher")
    public AbstractMessagePublisher mockMessagePublisher() {
        return Mockito.mock(AbstractMessagePublisher.class);
    }

}
