package com.qurasense.labApi.assay.eventhandler;

import com.qurasense.labApi.assay.events.AssayCreatedEvent;
import com.qurasense.labApi.assay.model.Assay;
import com.qurasense.labApi.assay.model.AssayUnit;
import com.qurasense.labApi.assay.repository.LabRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabEventHandler {
    @Autowired
    private LabRepository repository;

    @EventHandler
    public void on(AssayCreatedEvent event) {
        Assay assay = new Assay(event.id, event.name, AssayUnit.of(event.unitType), event.minValue, event.maxValue);
        repository.saveAssay(assay);
    }
}
