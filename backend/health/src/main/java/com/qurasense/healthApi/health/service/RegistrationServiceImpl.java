package com.qurasense.healthApi.health.service;

import java.util.Date;
import javax.annotation.Resource;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.broadcast.message.SignupMessage;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.model.HealthRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Resource(name = "broadcastMessagePublisher")
    private AbstractMessagePublisher broadcastMessagePublisher;

    @Override
    public String register(RegistrationData registrationData) {
        StopWatch sw = new StopWatch();
        sw.start("save health info");
        HealthInfo healthInfo = registrationData.getHealthInfo();
        if (healthInfo.getId() == null) {
            healthInfo.setId(idGenerationStrategy.generate(null));
        }
        String healthId = entityRepository.save(healthInfo);
        broadcastMessagePublisher.publishMessage(new SignupMessage(healthInfo.getUserId(), healthId));
        sw.stop();
        sw.start("save health record");
        HealthRecord healthRecord = registrationData.getHealthRecord();
        healthRecord.setParent(entityRepository.getParent(HealthRecord.class));
        healthRecord.setId(idGenerationStrategy.generate(null));
        healthRecord.setCreateTime(new Date());
        healthRecord.setCreateUserId(healthRecord.getCustomerUserId());
        entityRepository.save(healthRecord);
        logger.info("health record saved, id {}", healthRecord.getId());
        sw.stop();
        logger.info(sw.prettyPrint());
        return healthId;
    }

}
