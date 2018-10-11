package com.qurasense.communication.samples;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwilioSample {
    private static final Logger LOG = LoggerFactory.getLogger(TwilioSample.class);
    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACfe8d5f64d8955ab6265c000797e5bf69";
    public static final String AUTH_TOKEN = "0283e4c86b8232c694c9613f6a185437";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = null;
        try {
            message = Message.creator(new PhoneNumber("+7 927 036-74-19"),
                    new PhoneNumber("+1 909-326-7430"),
                    "Qurasense twillio sample").create();
            LOG.info("seneded, message sid: {message.getSid()}");
        } catch (ApiException e) {
            LOG.error("code: {}, statusCode: {}, message: {}", e.getCode(), e.getStatusCode(), e.getMessage(), e);
        }
    }
}