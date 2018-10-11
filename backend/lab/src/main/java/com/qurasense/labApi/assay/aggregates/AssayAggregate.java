package com.qurasense.labApi.assay.aggregates;

import com.qurasense.labApi.assay.commands.CreateAssayCommand;
import com.qurasense.labApi.assay.events.AssayCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class AssayAggregate {
    @AggregateIdentifier
    private String id;
    private String name;
    private String unitType;
    private double minValue;
    private double maxValue;

    @CommandHandler
    public AssayAggregate(CreateAssayCommand cmd) {
        apply(new AssayCreatedEvent(cmd.id, cmd.name, cmd.unitType, cmd.minValue, cmd.maxValue ));
    }

    @EventSourcingHandler
    public void on(AssayCreatedEvent event) {
        this.id = event.id;
        this.name = event.name;
        this.unitType = event.unitType;
        this.minValue = event.minValue;
        this.maxValue = event.maxValue;
    }
}
