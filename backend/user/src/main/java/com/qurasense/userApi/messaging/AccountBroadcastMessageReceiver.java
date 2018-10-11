package com.qurasense.userApi.messaging;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.pubsub.v1.PubsubMessage;
import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.messaging.broadcast.message.CustomerAddedToTrial;
import com.qurasense.common.messaging.broadcast.subscriber.BroadcastMessageReceiver;
import com.qurasense.userApi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountBroadcastMessageReceiver extends BroadcastMessageReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Override
    protected void process(String jsonString, String type, PubsubMessage message) throws IOException {
        if (CustomerAddedToTrial.class.getName().equals(type)) {
            processCustomerAddedToTrial(jsonString);
        } else {
            logger.info("ignore broadcast event, type: {}", type);
        }
    }

    private void processCustomerAddedToTrial(String jsonString) throws IOException {
        CustomerAddedToTrial customerAddedToTrial = objectMapper.readValue(jsonString, CustomerAddedToTrial.class);
        logger.info("setting user {}, ");
        customerAddedToTrial.getUserId();
        ObjectifyService.run(()->{
            userService.setActive(customerAddedToTrial.getUserId());
            return null;
        });
    }

}
