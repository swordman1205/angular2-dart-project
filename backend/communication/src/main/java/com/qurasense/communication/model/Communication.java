package com.qurasense.communication.model;

import com.google.cloud.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.messaging.messages.CommunicationMessage;
import com.qurasense.common.model.AbstractIdentifiable;

@Entity
public class Communication extends AbstractIdentifiable {

    @Parent
    private Key parent;
    @Index
    private String sourceUserId;
    private String destinationUserId;
    @Index
    private String messageType;
    private CommunicationMessage.ChannelType channelType;

    public Key getParent() {
        return parent;
    }

    public void setParent(Key parent) {
        this.parent = parent;
    }

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public String getDestinationUserId() {
        return destinationUserId;
    }

    public void setDestinationUserId(String destinationUserId) {
        this.destinationUserId = destinationUserId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public CommunicationMessage.ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(CommunicationMessage.ChannelType channelType) {
        this.channelType = channelType;
    }
}
