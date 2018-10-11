package com.qurasense.trial.events;

import java.time.Instant;
import java.util.List;

public class TrialCreatedEvent {
    public final String id;
    public final String name;
    public final Instant creationTime;
    public final String creationUserId;

    public TrialCreatedEvent(String id, String name, Instant creationTime, String creationUserId) {
        this.id = id;
        this.name = name;
        this.creationTime = creationTime;
        this.creationUserId = creationUserId;
    }
}
