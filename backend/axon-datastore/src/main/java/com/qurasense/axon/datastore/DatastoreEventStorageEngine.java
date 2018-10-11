package com.qurasense.axon.datastore;

import java.util.List;
import java.util.Optional;

import org.axonframework.common.ObjectUtils;
import org.axonframework.common.jdbc.PersistenceExceptionResolver;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.eventstore.AbstractEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.BatchingEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.DomainEventData;
import org.axonframework.eventsourcing.eventstore.TrackedEventData;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.axonframework.serialization.upcasting.event.NoOpEventUpcaster;
import org.axonframework.serialization.xml.XStreamSerializer;

public class DatastoreEventStorageEngine extends BatchingEventStorageEngine {

    private final DatastoreStrategy datastoreStrategy;

    /**
     * Initializes an EventStorageEngine with given {@code serializer}, {@code upcasterChain}, {@code
     * persistenceExceptionResolver}, {@code eventSerializer} and {@code batchSize}.
     *
     * @param serializer                   Used to serialize and deserialize snapshots. If {@code null}
     *                                     a {@link XStreamSerializer} is instantiated by the
     *                                     {@link AbstractEventStorageEngine}.
     * @param upcasterChain                Allows older revisions of serialized objects to be deserialized. If {@code
     *                                     null} a {@link NoOpEventUpcaster} is used.
     * @param persistenceExceptionResolver Detects concurrency exceptions from the backing database. If {@code null}
     *                                     persistence exceptions are not explicitly resolved.
     * @param eventSerializer              Used to serialize and deserialize event payload and metadata.
     *                                     If {@code null} a {@link XStreamSerializer} is instantiated by the
     *                                     {@link AbstractEventStorageEngine}.
     * @param batchSize                    The number of events that should be read at each database access. When more
     *                                     than this number of events must be read to rebuild an aggregate's state, the
     *                                     events are read in batches of this size. If {@code null} a batch size of 100
     *                                     is used. Tip: if you use a snapshotter, make sure to choose snapshot trigger
     *                                     and batch size such that a single batch will generally retrieve all events
     */
    public DatastoreEventStorageEngine(Serializer serializer, EventUpcaster upcasterChain,
            PersistenceExceptionResolver persistenceExceptionResolver, Serializer eventSerializer, Integer batchSize,
            DatastoreStrategy datastoreStrategy) {
        super(serializer, upcasterChain, ObjectUtils.getOrDefault(persistenceExceptionResolver,
                DatastorePersistanceExceptionResolver::new), eventSerializer, batchSize);
        this.datastoreStrategy = datastoreStrategy;
    }

    @Override
    protected List<? extends TrackedEventData<?>> fetchTrackedEvents(TrackingToken lastToken, int batchSize) {
        return datastoreStrategy.fetchTrackingEvents(lastToken, batchSize);
    }

    @Override
    protected List<? extends DomainEventData<?>> fetchDomainEvents(String aggregateIdentifier, long firstSequenceNumber, int batchSize) {
        return datastoreStrategy.fetchDomainEvents(aggregateIdentifier, firstSequenceNumber, batchSize);
    }

    @Override
    protected void appendEvents(List<? extends EventMessage<?>> events, Serializer serializer) {
        for (EventMessage eventMessage : events) {
            try {
                datastoreStrategy.appendEvent(eventMessage, serializer);
            } catch (Exception e) {
                handlePersistenceException(e, eventMessage);
            }
        }
    }

    @Override
    protected void storeSnapshot(DomainEventMessage<?> snapshot, Serializer serializer) {
        try {
            datastoreStrategy.appendSnapshot(snapshot, serializer);
            datastoreStrategy.deleteSnapshots(snapshot.getAggregateIdentifier(), snapshot.getSequenceNumber());
        } catch (Exception e) {
            handlePersistenceException(e, snapshot);
        }
    }

    @Override
    protected Optional<? extends DomainEventData<?>> readSnapshotData(String aggregateIdentifier) {
        return datastoreStrategy.findLastSnapshot(aggregateIdentifier);
    }
}
