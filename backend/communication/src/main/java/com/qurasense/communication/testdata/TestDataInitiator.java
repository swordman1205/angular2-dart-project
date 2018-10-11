package com.qurasense.communication.testdata;

import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.communication.model.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"test"})
@Component
public class TestDataInitiator {

    private Logger logger = LoggerFactory.getLogger(TestDataInitiator.class);


    @Autowired
    private EntityRepository entityRepository;

    @PostConstruct
    protected void initTestData() {
        ObjectifyService.init();
        ObjectifyService.run(() -> {
            int deletedComunications = entityRepository.deleteAll(Communication.class);
            logger.info("deleted {} communications", deletedComunications);
            return null;
        });
    }

}
