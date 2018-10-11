package com.qurasense.axon.datastore.event;

import java.time.Instant;
import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.qurasense.axon.datastore.EventEntryConfiguration;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.serialization.SerializedMetaData;
import org.axonframework.serialization.SerializedObject;
import org.axonframework.serialization.SimpleSerializedObject;

/**
 * Implementation of a serialized event message that can be used to create a Mongo document.
 */
public class EventEntry implements DomainEventData<Object> {

    private final String aggregateIdentifier;
    private final String aggregateType;
    private final long sequenceNumber;
    private final Date timestamp;
    private final String serializedPayload;
    private final String payloadType;
    private final String payloadRevision;
    private final String serializedMetaData;
    private final String eventIdentifier;

    public EventEntry(Entity entity, EventEntryConfiguration configuration) {
        eventIdentifier = entity.getKey().getName();
        aggregateIdentifier = (String) entity.getString(configuration.aggregateIdentifierProperty());
        aggregateType = (String) entity.getString(configuration.typeProperty());
        sequenceNumber = entity.getLong(configuration.sequenceNumberProperty());
        serializedPayload = entity.getString(configuration.payloadProperty());
        timestamp = entity.getTimestamp(configuration.timestampProperty()).toSqlTimestamp();
        payloadType = entity.getString(configuration.payloadTypeProperty());
        payloadRevision = entity.getString(configuration.payloadRevisionProperty());
        serializedMetaData = entity.getString(configuration.metaDataProperty());
    }

    @Override
    public String getType() {
        return aggregateType;
    }

    @Override
    public String getAggregateIdentifier() {
        return aggregateIdentifier;
    }

    @Override
    public long getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String getEventIdentifier() {
        return eventIdentifier;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp.toInstant();
    }

    @Override
    @SuppressWarnings("unchecked")
    public SerializedObject<Object> getMetaData() {
        return new SerializedMetaData(serializedMetaData, getRepresentationType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public SerializedObject<Object> getPayload() {
        return new SimpleSerializedObject(serializedPayload, getRepresentationType(), payloadType, payloadRevision);
    }

    private Class<?> getRepresentationType() {
        Class<?> representationType = String.class;
        return representationType;
    }
}
