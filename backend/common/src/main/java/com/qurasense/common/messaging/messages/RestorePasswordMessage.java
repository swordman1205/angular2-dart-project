package com.qurasense.common.messaging.messages;

public class RestorePasswordMessage extends CommunicationMessage {

    private String url;

    public RestorePasswordMessage() {
    }

    public RestorePasswordMessage(String url, String address, ChannelType type) {
        super(address, type);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
