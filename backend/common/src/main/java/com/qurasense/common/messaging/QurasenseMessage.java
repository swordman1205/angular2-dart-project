package com.qurasense.common.messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties()
public class QurasenseMessage {
    private QurasenseMessageType type;
    private Long userId;
    private String authToken;

    public QurasenseMessage() {
    }

    public QurasenseMessage(QurasenseMessageType type, Long userId, String authToken) {
        this.type = type;
        this.userId = userId;
        this.authToken = authToken;
    }

    public QurasenseMessageType getType() {
        return type;
    }

    public void setType(QurasenseMessageType type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public static enum QurasenseMessageType {
        USER_CREATED, USER_AUTHENTICATED;
    }

}
