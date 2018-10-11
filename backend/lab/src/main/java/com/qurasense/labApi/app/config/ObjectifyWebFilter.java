package com.qurasense.labApi.app.config;

import java.time.LocalDate;
import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.datastore.LocalDateTranslator;
import com.qurasense.labApi.assay.model.Assay;
import com.qurasense.labApi.trial.model.SessionStatusChangeToken;
import com.qurasense.labApi.trial.model.Trial;
import com.qurasense.labApi.trial.model.TrialParticipant;
import com.qurasense.labApi.trial.model.TrialSession;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile({"emulator", "cloud"})
public class ObjectifyWebFilter extends ObjectifyFilter {

    @PostConstruct
    protected void addEntities() {
        ObjectifyService.init();
        ObjectifyService.factory().getTranslators().add(new LocalDateTranslator(LocalDate.class));
        ObjectifyService.register(Assay.class);
        ObjectifyService.register(Trial.class);
        ObjectifyService.register(TrialParticipant.class);
        ObjectifyService.register(TrialSession.class);
        ObjectifyService.register(SessionStatusChangeToken.class);
    }
}
