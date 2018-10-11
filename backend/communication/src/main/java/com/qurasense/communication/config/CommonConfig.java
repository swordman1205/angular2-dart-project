package com.qurasense.communication.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.qurasense.common.SimpleMicroserviceRegistry;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.datastore.UUIDIdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.datastore.DatastoreEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class CommonConfig {

    @Bean
    @Primary
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
    }

    @Bean
    public SimpleMicroserviceRegistry simpleMicoserviceRegistry() {
        return new SimpleMicroserviceRegistry();
    }

    @Bean
    public EntityRepository simpleRepository() {
        return new DatastoreEntityRepository();
    }

    @Bean
    @Profile({"cloud", "emulator"})
    public Datastore datastore() {
        return DatastoreOptions.getDefaultInstance().getService();
    }

    @Bean
    public IdGenerationStrategy idGenerationStrategy() {
        return new UUIDIdGenerationStrategy();
    }

}