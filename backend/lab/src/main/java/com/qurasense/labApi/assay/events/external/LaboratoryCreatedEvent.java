package com.qurasense.labApi.assay.events.external;

public class LaboratoryCreatedEvent {
    public final String id;
    public final String name;

    public LaboratoryCreatedEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
