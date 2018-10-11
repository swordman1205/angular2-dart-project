package com.qurasense.userApi.config;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Transaction;
import com.qurasense.common.MessagesRetriever;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.datastore.UUIDIdGenerationStrategy;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.common.repository.local.LocalEntityRepository;
import com.qurasense.common.token.TokenGenerator;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfiguration {
    @MockBean
    private MessagesRetriever messagesRetriever;

    @Bean
    public Datastore mockDatastore() {
        Datastore mock = Mockito.mock(Datastore.class);
        Mockito.when(mock.newTransaction()).thenReturn(Mockito.mock(Transaction.class));
        return mock;
    }

    @Bean("communicationMessagePublisher")
    public AbstractMessagePublisher mockMessagePublisher() {
        return Mockito.mock(AbstractMessagePublisher.class);
    }

    @Bean("broadcastMessagePublisher")
    public AbstractMessagePublisher mockBroadcastMessagePublisher() {
        return Mockito.mock(AbstractMessagePublisher.class);
    }

    @Bean
    public EntityRepository simpleRepository() {
        return new LocalEntityRepository();
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