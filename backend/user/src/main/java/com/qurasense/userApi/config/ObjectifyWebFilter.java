package com.qurasense.userApi.config;

import java.time.LocalDate;
import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyFilter;
import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.datastore.LocalDateTranslator;
import com.qurasense.userApi.model.CustomerInfo;
import com.qurasense.userApi.model.EmailConfirmationToken;
import com.qurasense.userApi.model.Organization;
import com.qurasense.userApi.model.User;
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
        ObjectifyService.register(User.class);
        ObjectifyService.register(CustomerInfo.class);
        ObjectifyService.register(EmailConfirmationToken.class);
        ObjectifyService.register(Organization.class);
    }

}
