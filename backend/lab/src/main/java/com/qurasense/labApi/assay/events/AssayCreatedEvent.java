package com.qurasense.labApi.assay.events;

public class AssayCreatedEvent {
    public final String id;
    public final String name;
    public final String unitType;
    public final double minValue;
    public final double maxValue;

    public AssayCreatedEvent(String id, String name, String unitType, double minValue, double maxValue) {
        this.id = id;
        this.name = name;
        this.unitType = unitType;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}
