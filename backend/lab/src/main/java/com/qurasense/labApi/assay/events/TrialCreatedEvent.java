package com.qurasense.labApi.assay.events;

import java.time.Instant;

public class TrialCreatedEvent {
    public String id;
    public String name;
    public Instant creationTime;
    public String creationUserId;

    public TrialCreatedEvent(String id, String name, Instant creationTime, String creationUserId) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.creationUserId = creationUserId;
    }
}
