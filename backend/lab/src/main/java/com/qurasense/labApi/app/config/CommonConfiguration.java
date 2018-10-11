package com.qurasense.labApi.app.config;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.qurasense.axon.datastore.DatastoreEventStorageEngine;
import com.qurasense.axon.datastore.DatastoreStrategy;
import com.qurasense.axon.datastore.EventEntryConfiguration;
import com.qurasense.axon.datastore.ds.AncestorProvider;
import com.qurasense.axon.datastore.ds.DefaultRepository;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.datastore.UUIDIdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.datastore.DatastoreEntityRepository;
import com.qurasense.common.token.TokenGenerator;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class CommonConfiguration {
    @Autowired
    private Environment env;

    @Bean
    @Profile({"cloud", "emulator"})
    public EventStorageEngine eventStorageEngine() {
        EventEntryConfiguration configuration = EventEntryConfiguration.builder().build();
        AncestorProvider ancestorProvider = new AncestorProvider(datastore());
        DefaultRepository repository = new DefaultRepository(datastore(), configuration, ancestorProvider);
        DatastoreStrategy datastoreStrategy = new DatastoreStrategy(repository, configuration, null);
        return new DatastoreEventStorageEngine(null, null, null,null,
                null, datastoreStrategy);
    }

    @Bean
    @Profile({"cloud", "emulator"})
    public Datastore datastore() {
        return DatastoreOptions.getDefaultInstance().getService();
    }

    @Bean
    @Profile({"cloud", "emulator"})
    public EntityRepository simpleRepository() {
        return new DatastoreEntityRepository();
    }

    @Bean
    public IdGenerationStrategy idGenerationStrategy() {
        return new UUIDIdGenerationStrategy();
    }


    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator();
    }
}