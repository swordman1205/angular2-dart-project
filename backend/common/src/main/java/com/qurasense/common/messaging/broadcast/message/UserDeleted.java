package com.qurasense.common.messaging.broadcast.message;

public class UserDeleted implements BroadcastMessage {
    private String userId;
    private String role;

    public UserDeleted() {
    }

    public UserDeleted(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
