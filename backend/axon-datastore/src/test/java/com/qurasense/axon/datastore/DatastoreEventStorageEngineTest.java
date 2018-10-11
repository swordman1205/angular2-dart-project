package com.qurasense.axon.datastore;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.qurasense.axon.datastore.ds.AncestorProvider;
import com.qurasense.axon.datastore.ds.DefaultRepository;
import org.axonframework.common.IdentifierFactory;
import org.axonframework.common.jdbc.PersistenceExceptionResolver;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventsourcing.DomainEventMessage;
import org.axonframework.eventsourcing.GenericDomainEventMessage;
import org.axonframework.eventsourcing.eventstore.AbstractEventStorageEngineTest;
import org.axonframework.messaging.MetaData;
import org.axonframework.serialization.upcasting.event.EventUpcaster;
import org.axonframework.serialization.upcasting.event.NoOpEventUpcaster;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

//To make this test work add following to IDEA Environment variables:
//DATASTORE_DATASET=qurasense-test-1
//DATASTORE_EMULATOR_HOST=localhost:8380
//DATASTORE_EMULATOR_HOST_PATH=localhost:8380/datastore
//DATASTORE_HOST=http://localhost:8380
//DATASTORE_PROJECT_ID=qurasense-test-1
//GOOGLE_APPLICATION_CREDENTIALS=C:\Work\qurasense\qurasense\QurasenseTest.json
@Ignore
public class DatastoreEventStorageEngineTest extends AbstractEventStorageEngineTest {

    private static final Logger logger = LoggerFactory.getLogger(DatastoreEventStorageEngineTest.class);
    public static final String PAYLOAD = "payload", AGGREGATE = "aggregate", TYPE = "type";
    public static final MetaData METADATA = MetaData.emptyInstance();
    private DatastoreEventStorageEngine testSubject;
    private DefaultRepository repository;


    @Before
    public void init() {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        EventEntryConfiguration configuration = EventEntryConfiguration.builder().build();
        AncestorProvider ancestorProvider = new AncestorProvider(datastore);
        repository = new DefaultRepository(datastore, configuration, ancestorProvider);
        DatastoreStrategy datastoreStrategy = new DatastoreStrategy(repository, configuration, null);
        testSubject = new DatastoreEventStorageEngine(null, null, null, null,
                null, datastoreStrategy);
        setTestSubject(testSubject);
    }

    @After
    public void cleanDatastore() {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        EventEntryConfiguration configuration = EventEntryConfiguration.builder().build();
        AncestorProvider ancestorProvider = new AncestorProvider(datastore);
        DefaultRepository repository = new DefaultRepository(datastore, configuration, ancestorProvider);
        repository.clearAll();
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void testLoad_LargeAmountOfEvents() {
        int eventCount = testSubject.batchSize() + 10;
        testSubject.appendEvents(createEvents(eventCount));
        assertEquals(eventCount, testSubject.readEvents(AGGREGATE).asStream().count());
        assertEquals(eventCount, testSubject.readEvents(AGGREGATE).asStream().reduce((a, b) -> b).get().getSequenceNumber());
    }

    public List<DomainEventMessage<?>> createEvents(int numberOfEvents) {
        int endExclusive = numberOfEvents + 1;
        return IntStream.range(1, endExclusive).mapToObj(
                sequenceNumber -> createEvent(TYPE, IdentifierFactory.getInstance().generateIdentifier(), AGGREGATE,
                        sequenceNumber, PAYLOAD + sequenceNumber, METADATA))
                .collect(Collectors.toList());
    }

    public static DomainEventMessage<String> createEvent(String type, String eventId, String aggregateId,
            long sequenceNumber, String payload, MetaData metaData) {
        return new GenericDomainEventMessage<>(type, aggregateId, sequenceNumber, payload, metaData, eventId,
                GenericDomainEventMessage.clock.instant());
    }


    @Test
    @Override
    public void testUniqueKeyConstraintOnEventIdentifier() {
        logger.info("Unique event identifier is not currently guaranteed in the Datastore Event Storage Engine");
    }

    @Test
    public void testOnlySingleSnapshotRemains() {
        testSubject.storeSnapshot(createEvent(0));
        testSubject.storeSnapshot(createEvent(1));
        testSubject.storeSnapshot(createEvent(2));

        assertEquals(1, repository.findSnapshotCount());
    }

    @Test
    public void testFetchHighestSequenceNumber() {
        testSubject.appendEvents(createEvent(1), createEvent(2));
        testSubject.appendEvents(createEvent(3));

        assertEquals(3, (long) testSubject.lastSequenceNumberFor(AGGREGATE).get());
        assertFalse(testSubject.lastSequenceNumberFor("notexist").isPresent());
    }

    public DomainEventMessage<String> createEvent(long sequenceNumber) {
        return createEvent(AGGREGATE, sequenceNumber);
    }

    public DomainEventMessage<String> createEvent(String aggregateId, long sequenceNumber) {
        return createEvent(aggregateId, sequenceNumber, PAYLOAD);
    }

    public DomainEventMessage<String> createEvent(String aggregateId, long sequenceNumber, String payload) {
        return createEvent(TYPE, IdentifierFactory.getInstance().generateIdentifier(), aggregateId, sequenceNumber,
                payload, METADATA);
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void testLoadTrackedEvents() throws InterruptedException {
        testSubject.appendEvents(createEvents(4));
        assertEquals(4, testSubject.readEvents(null, false).count());

        // give the clock some time to make sure the last message is really last
        Thread.sleep(10);

        DomainEventMessage<?> eventMessage = createEvent("otherAggregate", 0);
        testSubject.appendEvents(eventMessage);
        assertEquals(5, testSubject.readEvents(null, false).count());
        List<? extends TrackedEventMessage<?>> list = testSubject.readEvents(null, false).collect(Collectors.toList());
        String identifier = testSubject.readEvents(null, false).reduce((a, b) -> b).get().getIdentifier();
        assertEquals(eventMessage.getIdentifier(), identifier);
    }

    protected DatastoreEventStorageEngine createEngine(EventUpcaster upcasterChain) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        EventEntryConfiguration configuration = EventEntryConfiguration.builder().build();
        AncestorProvider ancestorProvider = new AncestorProvider(datastore);
        DefaultRepository repository = new DefaultRepository(datastore, configuration, ancestorProvider);
        DatastoreStrategy datastoreStrategy = new DatastoreStrategy(repository, configuration, null);
        return new DatastoreEventStorageEngine(null, upcasterChain, null,null,
                null, datastoreStrategy);
    }

    protected DatastoreEventStorageEngine createEngine(PersistenceExceptionResolver persistenceExceptionResolver) {
        XStreamSerializer serializer = new XStreamSerializer();
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        EventEntryConfiguration configuration = EventEntryConfiguration.builder().build();
        AncestorProvider ancestorProvider = new AncestorProvider(datastore);
        DefaultRepository repository = new DefaultRepository(datastore, configuration, ancestorProvider);
        DatastoreStrategy datastoreStrategy = new DatastoreStrategy(repository, configuration, null);
        return new DatastoreEventStorageEngine(serializer, NoOpEventUpcaster.INSTANCE, persistenceExceptionResolver,null,
                null, datastoreStrategy);
    }
}
