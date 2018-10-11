package com.qurasense.labApi.trial.aggregates;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import com.qurasense.labApi.trial.commands.CreateTrialCommand;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.trial.events.TrialCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class TrialAggregate {
    @AggregateIdentifier
    private String id;
    private String name;
    private Set<String> testIds;
    private Instant creationTime;
    private String creationUserId;
    private List<TrialParticipant> participants;
    private List<String> assayIds;

    @CommandHandler
    public TrialAggregate(CreateTrialCommand cmd) {
        apply(new TrialCreatedEvent(cmd.id, cmd.name, Instant.now(), cmd.creationUserId));
    }

    @EventSourcingHandler
    public void on(TrialCreatedEvent event) {
        this.id = event.id;
        this.name = event.name;
        this.creationTime = event.creationTime;
        this.creationUserId = event.creationUserId;
    }
}