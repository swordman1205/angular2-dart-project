package com.qurasense.communication.rules;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurasense.common.messaging.broadcast.message.SignupMessage;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.communication.swu.SwuSender;
import com.qurasense.communication.swu.provider.builder.SignupProviderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SignupProcessor extends DefaultProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private SignupProviderBuilder signupProviderBuilder;

    @Autowired
    private SwuSender swuSender;

    private SignupMessage signupMessage;

    @Override
    public TypeProcessor create(String message) {
        try {
            signupMessage = objectMapper.readerFor(SignupMessage.class).readValue(message);
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public TypeProcessor process() {
        swuSender.send(signupProviderBuilder.build(signupMessage), supportType());
        return this;
    }

    @Override
    public String supportType() {
        return SignupMessage.class.getName();
    }
}
