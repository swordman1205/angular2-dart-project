package com.qurasense.axon.datastore.event;

import java.time.Instant;

import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.TrackedEventData;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.serialization.SerializedObject;

public class TrackedDatastoreEventEntry<T> implements DomainEventData<T>, TrackedEventData<T> {

    private final DomainEventData<T> delegate;
    private final TrackingToken trackingToken;

    public TrackedDatastoreEventEntry(DomainEventData<T> delegate, TrackingToken trackingToken) {
        this.delegate = delegate;
        this.trackingToken = trackingToken;
    }

    @Override
    public String getType() {
        return delegate.getType();
    }

    @Override
    public String getAggregateIdentifier() {
        return delegate.getAggregateIdentifier();
    }

    @Override
    public long getSequenceNumber() {
        return delegate.getSequenceNumber();
    }

    @Override
    public TrackingToken trackingToken() {
        return trackingToken;
    }

    @Override
    public String getEventIdentifier() {
        return delegate.getEventIdentifier();
    }

    @Override
    public Instant getTimestamp() {
        return delegate.getTimestamp();
    }

    @Override
    public SerializedObject<T> getMetaData() {
        return delegate.getMetaData();
    }

    @Override
    public SerializedObject<T> getPayload() {
        return delegate.getPayload();
    }
}