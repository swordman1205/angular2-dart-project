package com.qurasense.communication.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import com.qurasense.common.messaging.messages.CommunicationMessage;
import com.qurasense.common.messaging.messages.RestorePasswordMessage;
import com.qurasense.common.messaging.messages.SmsMessage;
import com.qurasense.common.messaging.messages.SuccessSignupMessage;
import com.qurasense.communication.swu.provider.RestorePasswordDataProvider;
import com.qurasense.communication.swu.provider.SuccessSignupDataProvider;
import com.qurasense.communication.swu.SwuSender;
import com.qurasense.communication.twilio.TwilioSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommunicationMessageReceiver implements MessageReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SwuSender swuSender;

    @Autowired
    private TwilioSender twilioSender;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        logger.info("message received");
        try {
            String jsonString = message.getData().toStringUtf8();
            CommunicationMessage cm = objectMapper.readerFor(CommunicationMessage.class).readValue(jsonString);
            if (cm instanceof SuccessSignupMessage) {
                SuccessSignupDataProvider successSignupDataProvider =
                        applicationContext.getBean(SuccessSignupDataProvider.class, (SuccessSignupMessage) cm);
                swuSender.send(successSignupDataProvider, "SuccessSignupMessage");
            } else if (cm instanceof RestorePasswordMessage) {
                swuSender.send(new RestorePasswordDataProvider((RestorePasswordMessage) cm), "RestorePasswordMessage");
            } else if (cm instanceof SmsMessage) {
                twilioSender.send(cm.getAddress(), ((SmsMessage) cm).getText());
            } else {
                throw new IllegalStateException("other types not yet supported");
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        consumer.ack();
    }

}
