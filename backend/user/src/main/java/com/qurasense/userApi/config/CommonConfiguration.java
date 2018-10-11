package com.qurasense.userApi.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.qurasense.common.MessagesRetriever;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.datastore.UUIDIdGenerationStrategy;
import com.qurasense.common.messaging.broadcast.publisher.BroadcastMessagePublisher;
import com.qurasense.common.messaging.broadcast.publisher.EmulatorBroadcastMessagePublisher;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.datastore.DatastoreEntityRepository;
import com.qurasense.common.token.TokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

@Configuration
public class CommonConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Environment env;

    @Bean
    @Profile({"cloud", "emulator"})
    public Datastore datastore() {
        String googleApplicationCredentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        logger.info("Setting up datastore, GOOGLE_APPLICATION_CREDENTIALS: {}", googleApplicationCredentials);
        if (StringUtils.isNotBlank(googleApplicationCredentials)) {
            try {
                Path path = Paths.get(googleApplicationCredentials);
                boolean exists = Files.exists(path);
                logger.info("{} exist: {}", googleApplicationCredentials, exists);
                if (logger.isDebugEnabled()) {
                    String keyFileContent = new String(Files.readAllBytes(path));
                    logger.debug(keyFileContent);
                }
            } catch (Exception e) {
                logger.error("Error while chech if googleApplicationCredentials exists", e);
            }
        }
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        return datastore;
    }

    @Bean
    public MessagesRetriever messagesRetriever() {
        return new MessagesRetriever();
    }

    @Bean
    @Profile({"cloud", "emulator"})
    public EntityRepository simpleRepository() {
        return new DatastoreEntityRepository();
    }

    @Bean(name = "uuidIdGenerationStrategy")
    public IdGenerationStrategy uuidIdGenerationStrategy() {
        return new UUIDIdGenerationStrategy();
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

    @Bean
    public TokenGenerator tokenGenerator() {
        return new TokenGenerator();
    }

}