package com.qurasense.common.messaging.broadcast.message;

public class SessionCreated implements BroadcastMessage {
    private String userId;
    private String sampleId;
    private String nurseId;
    private String sessionStatusChangeToken;

    public SessionCreated() {
    }

    public SessionCreated(String userId, String sampleId, String nurseId, String sessionStatusChangeToken) {
        this.userId = userId;
        this.sampleId = sampleId;
        this.nurseId = nurseId;
        this.sessionStatusChangeToken = sessionStatusChangeToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }

    public String getNurseId() {
        return nurseId;
    }

    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }

    public String getSessionStatusChangeToken() {
        return sessionStatusChangeToken;
    }

    public void setSessionStatusChangeToken(String sessionStatusChangeToken) {
        this.sessionStatusChangeToken = sessionStatusChangeToken;
    }
}
