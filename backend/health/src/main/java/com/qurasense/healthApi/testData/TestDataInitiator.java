package com.qurasense.healthApi.testData;

import javax.annotation.PostConstruct;

import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.model.HealthRecord;
import com.qurasense.healthApi.health.repository.HealthRecordRepository;
import com.qurasense.healthApi.sample.model.StripSample;
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
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private EntityRepository entityRepository;

    @PostConstruct
    protected void initTestData() {
        ObjectifyService.run(() -> {
            int deletedSamples = entityRepository.deleteAll(StripSample.class);
            int deletedHealthInfo = entityRepository.deleteAll(HealthInfo.class);
            int deletedHealthRecords = entityRepository.deleteAll(HealthRecord.class);
            logger.info("deleted {} samples, {} health records, {} health infos", deletedSamples, deletedHealthRecords,
                    deletedHealthInfo);
            return null;
        });
    }

}
