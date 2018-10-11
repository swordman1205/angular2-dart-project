package com.qurasense.common.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class AbstractMessagePublisher {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Qualifier("jsonObjectMapper")
    @Autowired
    private ObjectMapper objectMapper;

    protected abstract Publisher getPublisher();

    public void publishMessage(Object messageObject) {
        try {
            publishMessage(objectMapper.writer().writeValueAsString(messageObject), messageObject.getClass().getName());
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     *
     * @param message
     * @return messageId
     * @throws Exception
     */
    public String publishMessage(String message, String type) throws Exception {
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .putAttributes("type", type)
                .setData(data)
                .build();
        String messageId = getPublisher().publish(pubsubMessage).get();
        logger.info("message:{} was sended id: {}", message, messageId);
        return messageId;
    }
}
