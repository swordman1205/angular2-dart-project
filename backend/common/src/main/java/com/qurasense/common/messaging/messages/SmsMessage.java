package com.qurasense.common.messaging.messages;

public class SmsMessage extends CommunicationMessage {

    private String text;

    public SmsMessage(String address, String text) {
        super(address, ChannelType.SMS);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
