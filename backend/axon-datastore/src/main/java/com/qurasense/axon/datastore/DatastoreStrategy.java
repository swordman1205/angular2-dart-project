package com.qurasense.axon.datastore;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.QueryResults;
import com.qurasense.axon.datastore.ds.DefaultRepository;
import com.qurasense.axon.datastore.event.DatastoreTrackingToken;
import com.qurasense.axon.datastore.event.EventEntry;
import com.qurasense.axon.datastore.event.TrackedDatastoreEventEntry;
import org.axonframework.common.Assert;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.EventUtils;
import org.axonframework.eventsourcing.eventstore.TrackedEventData;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.messaging.Message;
import org.axonframework.serialization.SerializationAware;
import org.axonframework.serialization.SerializedObject;
import org.axonframework.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.axonframework.common.ObjectUtils.getOrDefault;

public class DatastoreStrategy {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DefaultRepository defaultRepository;
    private final EventEntryConfiguration configuration;
    private final Duration lookBackTime;

    public DatastoreStrategy(DefaultRepository defaultPersister, EventEntryConfiguration configuration, Duration lookBackTime) {
        this.defaultRepository = defaultPersister;
        this.configuration = configuration;
        this.lookBackTime = getOrDefault(lookBackTime, Duration.ofMillis(1000L));
    }

    public void appendEvent(EventMessage event, Serializer serializer) {
        SerializedObject<String> serializedPayload = serializePayload(event, serializer);
        SerializedObject<String> serializedMetadata = serializeMetaData(event, serializer);
        defaultRepository.persist(serializedPayload, serializedMetadata, EventUtils.asDomainEventMessage(event));
        logger.info("persisted event {} with timestamp {}", event.getIdentifier(), event.getTimestamp());
    }

    public void appendSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
        SerializedObject<String> serializedPayload = serializePayload(snapshot, serializer);
        SerializedObject<String> serializedMetadata = serializeMetaData(snapshot, serializer);
        defaultRepository.persistSnapshot(serializedPayload, serializedMetadata, snapshot);
    }

    public SerializedObject<String> serializePayload(Message<?> message, Serializer serializer) {
        if (message instanceof SerializationAware) {
            return ((SerializationAware) message).serializePayload(serializer, String.class);
        }
        return serializer.serialize(message.getPayload(), String.class);
    }

    public static SerializedObject<String> serializeMetaData(Message<?> message, Serializer serializer) {
        if (message instanceof SerializationAware) {
            return ((SerializationAware) message).serializeMetaData(serializer, String.class);
        }
        return serializer.serialize(message.getMetaData(), String.class);
    }

    public List<? extends DomainEventData<?>> fetchDomainEvents(String aggregateIdentifier, long firstSequenceNumber, int batchSize) {
        QueryResults<Entity> queryResults = defaultRepository.fetch(aggregateIdentifier, firstSequenceNumber, batchSize);
        List<EventEntry> result = new ArrayList<>();
        while (queryResults.hasNext()) {
            result.add(new EventEntry(queryResults.next(), configuration));
        }
        return result;
    }

    public List<? extends TrackedEventData<?>> fetchTrackingEvents(TrackingToken lastToken, int batchSize) {
        QueryResults<Entity> queryResults;

        if (lastToken == null) {
            queryResults = defaultRepository.fetch(batchSize);
        } else {
            Assert.isTrue(lastToken instanceof DatastoreTrackingToken, () -> String.format("Token %s is of the wrong type", lastToken));
            DatastoreTrackingToken trackingToken = (DatastoreTrackingToken) lastToken;
            Instant fetchTimestamp = trackingToken.getTimestamp().minus(lookBackTime);
            queryResults = defaultRepository.fetch(fetchTimestamp, batchSize, configuration);
        }

        AtomicReference<DatastoreTrackingToken> previousToken = new AtomicReference<>((DatastoreTrackingToken) lastToken);
        List<TrackedEventData<?>> results = new ArrayList<>();
        while (queryResults.hasNext()) {
            Entity entity = queryResults.next();
            EventEntry eventEntry = new EventEntry(entity, configuration);
            logger.info("fetched tracking event {} with timestamp {}", eventEntry.getEventIdentifier(), eventEntry.getTimestamp());
            if (previousToken.get() != null &&
                    previousToken.get().getKnownEventIds().contains(eventEntry.getEventIdentifier())) {
                continue;
            }
            TrackedDatastoreEventEntry<Object> trackedDatastoreEventEntry = new TrackedDatastoreEventEntry<>(eventEntry,
                    previousToken.updateAndGet(token -> token == null
                    ? DatastoreTrackingToken.of(eventEntry.getTimestamp(), eventEntry.getEventIdentifier())
                    : token.advanceTo(eventEntry.getTimestamp(), eventEntry.getEventIdentifier(), lookBackTime)));
            results.add(trackedDatastoreEventEntry);
        }
        return results;
    }

    public void deleteSnapshots(String aggregateIdentifier, long sequenceNumber) {
        defaultRepository.deleteSnapshots(aggregateIdentifier, sequenceNumber);
    }

    public Optional<? extends DomainEventData<?>> findLastSnapshot(String aggregateIdentifier) {
        QueryResults<Entity> queryResults = defaultRepository.findLastSnapshot(aggregateIdentifier);
        if (queryResults.hasNext()) {
            return Optional.of(new EventEntry(queryResults.next(), configuration));
        }
        return Optional.empty();
    }

}
