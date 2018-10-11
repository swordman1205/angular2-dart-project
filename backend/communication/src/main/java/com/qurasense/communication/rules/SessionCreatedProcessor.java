package com.qurasense.communication.rules;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.datastore.Key;
import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.messaging.broadcast.message.SessionCreated;
import com.qurasense.common.messaging.messages.CommunicationMessage;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.communication.model.Communication;
import com.qurasense.communication.swu.SwuSender;
import com.qurasense.communication.swu.provider.builder.SessionCreatedProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SessionCreatedProcessor extends DefaultProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private SessionCreatedProviderBuilder sessionCreatedProviderBuilder;

    @Autowired
    private SwuSender swuSender;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    private SessionCreated sessionCreated;

    @Override
    public TypeProcessor create(String message) {
        try {
            sessionCreated = objectMapper.readerFor(SessionCreated.class).readValue(message);
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public TypeProcessor process() {
        Key parent = entityRepository.getParent(Communication.class);
        String messageTypeName = SessionCreated.class.getName();
        swuSender.send(sessionCreatedProviderBuilder.build(sessionCreated), supportType());

        Communication young = new Communication();
        young.setId(idGenerationStrategy.generate(null));
        young.setMessageType(messageTypeName);
        young.setChannelType(CommunicationMessage.ChannelType.EMAIL);
        young.setDestinationUserId(sessionCreated.getNurseId());
        young.setParent(parent);
        young.setSourceUserId(sessionCreated.getUserId());
        tryCallOnSend(young);

        return this;
    }

    @Override
    public String supportType() {
        return SessionCreated.class.getName();
    }

}
