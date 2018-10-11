package com.qurasense.common.messaging.broadcast.message;

public class SignupMessage implements BroadcastMessage {

    private String userId;
    private String healthId;

    public SignupMessage() {
    }

    public SignupMessage(String userId, String healthId) {
        this.userId = userId;
        this.healthId = healthId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHealthId() {
        return healthId;
    }

    public void setHealthId(String healthId) {
        this.healthId = healthId;
    }
}
