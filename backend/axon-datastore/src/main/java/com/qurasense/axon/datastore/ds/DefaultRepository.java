package com.qurasense.axon.datastore.ds;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Entity.Builder;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.KeyQuery;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.qurasense.axon.datastore.EventEntryConfiguration;
import com.qurasense.axon.datastore.utils.DataStoreUtils;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.serialization.SerializedObject;

public class DefaultRepository {
    private final Datastore datastore;
    private final KeyFactory eventKeyFactory;
    private final KeyFactory sequenceKeyFactory;
    private final KeyFactory snapshotKeyFactory;
    private final EventEntryConfiguration configuration;
    private final AncestorProvider ancestorProvider;

    public DefaultRepository(Datastore datastore, EventEntryConfiguration configuration, AncestorProvider provider) {
        this.datastore = datastore;
        eventKeyFactory = datastore.newKeyFactory()
                .addAncestor(provider.getAncestorPathElement())
                .setKind(configuration.entityName());
        sequenceKeyFactory = datastore.newKeyFactory()
                .addAncestor(provider.getAncestorPathElement())
                .setKind(configuration.sequenceEntityName());
        snapshotKeyFactory = datastore.newKeyFactory()
                .addAncestor(provider.getAncestorPathElement())
                .setKind(configuration.snapshotEntityName());
        this.ancestorProvider = provider;
        this.configuration = configuration;
    }

    public void clearAll() {
        QueryResults<Key> enitityKeyResults = datastore.run(Query.newKeyQueryBuilder()
                .setFilter(PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()))
                .setKind(configuration.entityName()).build());
        while (enitityKeyResults.hasNext()) {
            Key next = enitityKeyResults.next();
            datastore.delete(next);
        }
        QueryResults<Key> seqEntityKeyResults = datastore.run(Query.newKeyQueryBuilder()
                .setFilter(PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()))
                .setKind(configuration.sequenceEntityName()).build());
        while (seqEntityKeyResults.hasNext()) {
            Key next = seqEntityKeyResults.next();
            datastore.delete(next);
        }
        QueryResults<Key> snapshotsKeyResults = datastore.run(Query.newKeyQueryBuilder()
                .setFilter(PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()))
                .setKind(configuration.snapshotEntityName()).build());
        while (snapshotsKeyResults.hasNext()) {
            Key next = snapshotsKeyResults.next();
            datastore.delete(next);
        }
    }

    public void persist(SerializedObject<String> serializedPayload, SerializedObject<String> serializedMetadata,
            DomainEventMessage<?> event) {
        Key eventKey = eventKeyFactory.newKey(event.getIdentifier());

        String seqKey = String.format("%s%s%s",
                event.getAggregateIdentifier(),
                configuration.aggregateSequenceDelimiter(),
                event.getSequenceNumber());
        Key sequenceKey = sequenceKeyFactory.newKey(seqKey);
        datastore.runInTransaction((drw)-> {
            drw.add(Entity.newBuilder(sequenceKey).build());

            Builder builder = Entity.newBuilder(eventKey);
            drw.add(buildEntity(serializedPayload, serializedMetadata, event, builder));
            return null;
        });
    }

    private Entity buildEntity(SerializedObject<String> serializedPayload, SerializedObject<String> serializedMetadata, DomainEventMessage<?> event, Builder builder) {
        return builder
                .set(configuration.payloadProperty(), DataStoreUtils.getValue(serializedPayload.getData()))
                .set(configuration.payloadTypeProperty(), DataStoreUtils.getValue(serializedPayload.getType().getName()))
                .set(configuration.payloadRevisionProperty(), DataStoreUtils.getValue(serializedPayload.getType().getRevision()))
                .set(configuration.metaDataProperty(), DataStoreUtils.getValue(serializedMetadata.getData()))
                .set(configuration.typeProperty(), DataStoreUtils.getValue(event.getType()))
                .set(configuration.sequenceNumberProperty(), event.getSequenceNumber())
                .set(configuration.aggregateIdentifierProperty(), event.getAggregateIdentifier())
                .set(configuration.timestampProperty(), DataStoreUtils.getValue(event.getTimestamp()))
                .build();
    }

    public void persistSnapshot(SerializedObject<String> serializedPayload, SerializedObject<String> serializedMetadata,
            DomainEventMessage<?> snapshot) {
        Key snapshotKey = snapshotKeyFactory.newKey(snapshot.getIdentifier());
        Entity entity = datastore.get(snapshotKey);
        Builder builder = entity != null ? Entity.newBuilder(entity) : Entity.newBuilder(snapshotKey);
        datastore.add(buildEntity(serializedPayload, serializedMetadata, snapshot, builder));
    }

    public QueryResults<Entity> fetch(String aggregateIdentifier, long firstSequenceNumber, int batchSize) {
        CompositeFilter compositeFilter = CompositeFilter.and(
                PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()),
                PropertyFilter.eq(configuration.aggregateIdentifierProperty(), aggregateIdentifier),
                PropertyFilter.ge(configuration.sequenceNumberProperty(), firstSequenceNumber)
        );
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind(configuration.entityName())
                .setFilter(compositeFilter)
                .setOrderBy(OrderBy.asc(configuration.sequenceNumberProperty()))
                .setLimit(batchSize)
                .build();
        return datastore.run(query);
    }

    public QueryResults<Entity> fetch(int batchSize) {
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setFilter(PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()))
                .setKind(configuration.entityName())
                .setOrderBy(
                        StructuredQuery.OrderBy.asc(configuration.timestampProperty()),
                        StructuredQuery.OrderBy.asc(configuration.sequenceNumberProperty())
                )
                .setLimit(batchSize)
                .build();
        return datastore.run(query);
    }

    public QueryResults<Entity> fetch(Instant timestamp, int batchSize, EventEntryConfiguration configuration) {
        CompositeFilter compositeFilter = CompositeFilter.and(
                PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()),
                StructuredQuery.PropertyFilter.ge(configuration.timestampProperty(), Timestamp.of(Date.from(timestamp)))
        );
        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind(configuration.entityName())
            .setFilter(compositeFilter)
            .setOrderBy(
                    StructuredQuery.OrderBy.asc(configuration.timestampProperty()),
                    StructuredQuery.OrderBy.asc(configuration.sequenceNumberProperty())
            )
            .setLimit(batchSize)
            .build();
        return datastore.run(query);
    }

    public void deleteSnapshots(String aggregateIdentifier, long sequenceNumber) {
        CompositeFilter compositeFilter = CompositeFilter.and(
                PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()),
                PropertyFilter.lt(configuration.sequenceNumberProperty(), sequenceNumber)
        );
        KeyQuery query = Query.newKeyQueryBuilder()
                .setKind(configuration.snapshotEntityName())
                .setFilter(compositeFilter)
                .build();
        QueryResults<Key> run = datastore.run(query);
        List<Key> keys = new ArrayList<>();
        run.forEachRemaining(keys::add);
        datastore.delete(keys.toArray(new Key[0]));
    }

    public QueryResults<Entity> findLastSnapshot(String aggregateIdentifier) {
        CompositeFilter compositeFilter = CompositeFilter.and(
                PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()),
                StructuredQuery.PropertyFilter.eq(configuration.aggregateIdentifierProperty(), aggregateIdentifier)
        );
        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind(configuration.snapshotEntityName())
            .setFilter(compositeFilter)
            .setOrderBy(
                StructuredQuery.OrderBy.desc(configuration.timestampProperty()),
                StructuredQuery.OrderBy.desc(configuration.sequenceNumberProperty())
            )
            .setLimit(1)
            .build();
        return datastore.run(query);
    }

    public int findSnapshotCount() {
        KeyQuery query = Query.newKeyQueryBuilder()
                .setFilter(PropertyFilter.hasAncestor(ancestorProvider.getAncestorKey()))
                .setKind(configuration.snapshotEntityName())
                .build();
        AtomicInteger ai = new AtomicInteger();
        datastore.run(query).forEachRemaining(key -> ai.incrementAndGet());
        return ai.get();
    }
}
