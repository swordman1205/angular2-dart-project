package com.qurasense.labApi.trial.eventhandler;

import java.sql.Date;

import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.trial.model.Trial;
import com.qurasense.trial.events.TrialCreatedEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrialEventHandler {
    @Autowired
    private EntityRepository entityRepository;

    @EventHandler
    public void on(TrialCreatedEvent event) {
        Trial trial = new Trial();
        trial.setCreateTime( Date.from(event.creationTime) );
        trial.setCreateUserId(event.creationUserId);
        trial.setId(event.id);
        trial.setName(event.name);
        entityRepository.save(trial);
    }
}
