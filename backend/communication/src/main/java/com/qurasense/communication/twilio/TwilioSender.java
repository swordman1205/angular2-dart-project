package com.qurasense.communication.twilio;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TwilioSender {

    private static final String ACCOUNT_SID = "ACfe8d5f64d8955ab6265c000797e5bf69";
    private static final String AUTH_TOKEN = "0283e4c86b8232c694c9613f6a185437";

    //from number from twilio, see https://www.twilio.com/console/phone-numbers/incoming
    private static final String FROM_NUMBER = "+1 909-326-7430";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    protected void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void send(String toNumber, String text) {
        Message message = null;
        try {
            message = Message.creator(new PhoneNumber(toNumber),
                    new PhoneNumber(FROM_NUMBER),
                    text).create();
            logger.info("sent to {}, message sid: {}, text: {}", toNumber, message.getSid(), text);
        } catch (ApiException e) {
            logger.error("code: {}, statusCode: {}, message: {}", e.getCode(), e.getStatusCode(), e.getMessage(), e);
        }
    }

}
