package com.qurasense.healthApi.app.config;

import java.time.LocalDate;
import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.datastore.LocalDateTranslator;
import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.model.HealthRecord;
import com.qurasense.healthApi.sample.model.Sample;
import com.qurasense.healthApi.sample.model.StripSample;
import com.qurasense.healthApi.sample.model.TubeSample;
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
        ObjectifyService.register(HealthInfo.class);
        ObjectifyService.register(HealthRecord.class);
        ObjectifyService.register(Sample.class);
        ObjectifyService.register(StripSample.class);
        ObjectifyService.register(TubeSample.class);
    }
}
