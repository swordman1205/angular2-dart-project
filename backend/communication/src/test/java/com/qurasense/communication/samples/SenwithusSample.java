package com.qurasense.communication.samples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qurasense.common.messaging.messages.CommunicationMessage;
import com.qurasense.common.messaging.messages.SuccessSignupMessage;
import com.sendwithus.SendWithUs;
import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.Email;
import com.sendwithus.model.SendReceipt;

public class SenwithusSample {
    public static final String SENDWITHUS_API_KEY = "test_b7fa341489091cb13076aa96f3b0af51e2075456";
    public static final String EMAIL_ID_WELCOME_EMAIL = "tem_hyxFdYSfPgPm38kH3C9W7S3X";

    public static void main(String[] args) {

        SendWithUs sendwithusAPI = new SendWithUs(SENDWITHUS_API_KEY);

        // Print list of available emails
        try {
            Email[] emails = sendwithusAPI.templates();
            for (int i = 0; i < emails.length; i++) {
                System.out.println(emails[i].toString());
            }
        } catch (SendWithUsException e) {
            System.out.println(e.toString());
        }

        List<SuccessSignupMessage> users = Arrays.asList(
            new SuccessSignupMessage("Søren Therkelsen","soren@qurasense.com", CommunicationMessage.ChannelType.EMAIL, "token"),
            new SuccessSignupMessage("Lars Tackmann","lars@qurasense.com", CommunicationMessage.ChannelType.EMAIL, "token"),
            new SuccessSignupMessage("Zufar Muhamadeev","zufarm@gmail.com", CommunicationMessage.ChannelType.EMAIL, "token")
        );

        for (SuccessSignupMessage user: users) {
            // Send Welcome Email
            Map<String, Object> recipientMap = new HashMap<String, Object>();
            recipientMap.put("name", user.getFullName()); // optional
            recipientMap.put("address", user.getAddress());

            // sender is optional
            Map<String, Object> senderMap = new HashMap<String, Object>();
            senderMap.put("name", "Qurasense"); // optional
            senderMap.put("address", "research@qurasense.com");
            senderMap.put("reply_to", "research@qurasense.com"); // optional

            // email data in to inject in the email template
            Map<String, Object> emailDataMap = new HashMap<>();
            emailDataMap.put("first_name", user.getFullName());
            emailDataMap.put("button_text", "OUR BELIEF");

            // Example sending a simple email
            try {
                SendReceipt sendReceipt = sendwithusAPI.send(
                        EMAIL_ID_WELCOME_EMAIL,
                        recipientMap,
                        senderMap,
                        emailDataMap
                );
                System.out.println(sendReceipt);
            } catch (SendWithUsException e) {
                System.out.println(e.toString());
            }
        }
    }

}
