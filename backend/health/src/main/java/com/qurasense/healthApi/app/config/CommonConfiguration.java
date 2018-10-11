package com.qurasense.healthApi.app.config;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.qurasense.common.MessagesRetriever;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.datastore.UUIDIdGenerationStrategy;
import com.qurasense.common.messaging.broadcast.publisher.BroadcastMessagePublisher;
import com.qurasense.common.messaging.broadcast.publisher.EmulatorBroadcastMessagePublisher;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.datastore.DatastoreEntityRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class CommonConfiguration {
    @Bean
    @Profile({"cloud", "emulator"})
    public Datastore datastore() {
        return DatastoreOptions.getDefaultInstance().getService();
    }

    @Bean(name = "uuidIdGenerationStrategy")
    public IdGenerationStrategy uuidIdGenerationStrategy() {
        return new UUIDIdGenerationStrategy();
    }

    @Bean
    @Profile({"cloud", "emulator"})
    public EntityRepository simpleRepository() {
        return new DatastoreEntityRepository();
    }

    @Bean
    public MessagesRetriever messagesRetriever() {
        return new MessagesRetriever();
    }

    @Bean("broadcastMessagePublisher")
    @Profile({"cloud"})
    public BroadcastMessagePublisher broadcastMessagePublisher() {
        return new BroadcastMessagePublisher();
    }

    @Bean("broadcastMessagePublisher")
    @Profile({"emulator"})
    public EmulatorBroadcastMessagePublisher emulatorBroadcastMessagePublisher() {
        return new EmulatorBroadcastMessagePublisher();
    }

}