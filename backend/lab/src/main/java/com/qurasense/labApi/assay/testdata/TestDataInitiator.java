package com.qurasense.labApi.assay.testdata;

import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.labApi.assay.model.Assay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"test"})
@Component("assayTestDataInitiator")
public class TestDataInitiator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityRepository entityRepositoryRepository;

    @PostConstruct
    protected void initTestData() {
        ObjectifyService.run(() -> {
            int deletedAssayCount = entityRepositoryRepository.deleteAll(Assay.class);
            logger.info("deleted {} assays", deletedAssayCount);
            return null;
        });
    }

}
