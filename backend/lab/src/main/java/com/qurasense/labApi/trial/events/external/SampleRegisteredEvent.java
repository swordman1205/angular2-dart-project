package com.qurasense.trial.events.external;

public class SampleRegisteredEvent {
    public final String sampleId;
    public final String customerId;

    public SampleRegisteredEvent(String sampleId, String customerId) {
        this.sampleId = sampleId;
        this.customerId = customerId;
    }
}
