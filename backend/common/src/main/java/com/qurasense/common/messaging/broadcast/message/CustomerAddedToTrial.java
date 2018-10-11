package com.qurasense.common.messaging.broadcast.message;

public class CustomerAddedToTrial implements BroadcastMessage {

    private String userId;

    public CustomerAddedToTrial() {
    }

    public CustomerAddedToTrial(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
