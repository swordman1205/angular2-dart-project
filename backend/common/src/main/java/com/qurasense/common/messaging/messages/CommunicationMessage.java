package com.qurasense.common.messaging.messages;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public class CommunicationMessage {

    private String address;
    private ChannelType type;

    public CommunicationMessage() {
    }

    public CommunicationMessage(String address, ChannelType type) {
        this.address = address;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public static enum ChannelType {
        EMAIL, SMS
    }

}
