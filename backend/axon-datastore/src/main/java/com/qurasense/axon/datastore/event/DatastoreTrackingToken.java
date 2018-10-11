package com.qurasense.axon.datastore.event;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.eventstore.TrackingToken;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Collections.unmodifiableSet;

//Copy of org.axonframework.mongo.eventsourcing.eventstore.MongoTrackingToken
public class DatastoreTrackingToken implements TrackingToken {

    private final long timestamp;
    private final Map<String, Long> trackedEvents;

    private DatastoreTrackingToken(long timestamp, Map<String, Long> trackedEvents) {
        this.timestamp = timestamp;
        this.trackedEvents = trackedEvents;
    }

    /**
     * Returns a new instance of a {@link DatastoreTrackingToken} with given {@code timestamp}, {@code eventIdentifier} and
     * {@code sequenceNumber} for the initial event in a stream.
     *
     * @param timestamp       the event's timestamp
     * @param eventIdentifier the event's identifier
     * @return initial Mongo tracking token instance
     */
    public static DatastoreTrackingToken of(Instant timestamp, String eventIdentifier) {
        return new DatastoreTrackingToken(timestamp.toEpochMilli(),
                                      Collections.singletonMap(eventIdentifier, timestamp.toEpochMilli()));
    }

    /**
     * Returns a new {@link DatastoreTrackingToken} instance based on this token but which has advanced to the event with
     * given {@code timestamp}, {@code eventIdentifier} and {@code sequenceNumber}. Prior events with a timestamp
     * smaller or equal than the latest event timestamp minus the given {@code lookBackTime} will not be included in the
     * new token.
     *
     * @param timestamp       the timestamp of the next event
     * @param eventIdentifier the maximum distance between a gap and the token's index
     * @param lookBackTime    the maximum time between the latest and oldest event stored in the new key
     * @return the new token that has advanced from the current token
     */
    public DatastoreTrackingToken advanceTo(Instant timestamp, String eventIdentifier, Duration lookBackTime) {
        if (trackedEvents.containsKey(eventIdentifier)) {
            throw new IllegalArgumentException(
                    String.format("The event to advance to [%s] should not be one of the token's known events",
                                  eventIdentifier));
        }
        long millis = timestamp.toEpochMilli();
        LinkedHashMap<String, Long> trackedEvents = new LinkedHashMap<>(this.trackedEvents);
        trackedEvents.put(eventIdentifier, millis);
        long newTimestamp = Math.max(millis, this.timestamp);
        return new DatastoreTrackingToken(newTimestamp, trim(trackedEvents, newTimestamp, lookBackTime));
    }

    private Map<String, Long> trim(LinkedHashMap<String, Long> priorEvents, long currentTime, Duration lookBackTime) {
        Long cutOffTimestamp = currentTime - lookBackTime.toMillis();
        Iterator<Long> iterator = priorEvents.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().compareTo(cutOffTimestamp) < 0) {
                iterator.remove();
            } else {
                return priorEvents;
            }
        }
        return priorEvents;
    }

    /**
     * Get the timestamp of the last event tracked by this token.
     *
     * @return the timestamp of the event with this token
     */
    public Instant getTimestamp() {
        return Instant.ofEpochMilli(timestamp);
    }

    /**
     * Returns an {@link Iterable} with all known identifiers of events tracked before and including this token. Note,
     * the token only stores ids of prior events if they are not too old, see
     * {@link #advanceTo(Instant, String, Duration)}.
     *
     * @return all known event identifiers
     */
    public Set<String> getKnownEventIds() {
        return unmodifiableSet(trackedEvents.keySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DatastoreTrackingToken that = (DatastoreTrackingToken) o;
        return timestamp == that.timestamp && Objects.equals(trackedEvents, that.trackedEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, trackedEvents);
    }

    @Override
    public String toString() {
        return "DatastoreTrackingToken{" + "timestamp=" + timestamp + ", trackedEvents=" + trackedEvents + '}';
    }

    @Override
    public TrackingToken lowerBound(TrackingToken other) {
        Assert.isTrue(other instanceof DatastoreTrackingToken, () -> "Incompatible token type provided.");
        DatastoreTrackingToken otherToken = (DatastoreTrackingToken) other;

        Map<String, Long> intersection = new HashMap<>(this.trackedEvents);
        trackedEvents.keySet().forEach(k -> {
            if (!otherToken.trackedEvents.containsKey(k)) {
                intersection.remove(k);
            }
        });
        return new DatastoreTrackingToken(min(timestamp, otherToken.timestamp), intersection);
    }

    @Override
    public TrackingToken upperBound(TrackingToken other) {
        Assert.isTrue(other instanceof DatastoreTrackingToken, () -> "Incompatible token type provided.");
        Long timestamp = max(((DatastoreTrackingToken)other).timestamp, this.timestamp);
        Map<String, Long> events = new HashMap<>(trackedEvents);
        events.putAll(((DatastoreTrackingToken) other).trackedEvents);
        return new DatastoreTrackingToken(timestamp, events);
    }

    @Override
    public boolean covers(TrackingToken other) {
        Assert.isTrue(other instanceof DatastoreTrackingToken, () -> "Incompatible token type provided.");
        DatastoreTrackingToken otherToken = (DatastoreTrackingToken) other;

        long oldest = this.trackedEvents.values().stream().min(Comparator.naturalOrder()).orElse(0L);
        return otherToken.timestamp <= this.timestamp
                && otherToken.trackedEvents.keySet().stream()
                                           .allMatch(k -> this.trackedEvents.containsKey(k) ||
                                                   otherToken.trackedEvents.get(k) < oldest);
    }
}
