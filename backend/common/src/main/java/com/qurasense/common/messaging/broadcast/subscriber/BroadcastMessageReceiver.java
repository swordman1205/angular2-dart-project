package com.qurasense.common.messaging.broadcast.subscriber;

import java.io.IOException;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import com.googlecode.objectify.ObjectifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BroadcastMessageReceiver implements MessageReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        String type = message.getAttributesOrDefault("type", null);
        String jsonString = message.getData().toStringUtf8();
        logger.info("message received, type: {}, message: {}", type, jsonString);
        ObjectifyService.run(() -> {
            try {
                process(jsonString, type, message);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                consumer.ack();
            }
            return null;
        });
    }

    protected abstract void process(String jsonString, String type, PubsubMessage message) throws IOException;

}
