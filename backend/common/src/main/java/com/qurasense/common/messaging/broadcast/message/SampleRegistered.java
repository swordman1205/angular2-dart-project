package com.qurasense.common.messaging.broadcast.message;

public class SampleRegistered implements BroadcastMessage {

    private String customerId;
    private String sampleId;

    public SampleRegistered() {
    }

    public SampleRegistered(String customerId, String sampleId) {
        this.customerId = customerId;
        this.sampleId = sampleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }
}
