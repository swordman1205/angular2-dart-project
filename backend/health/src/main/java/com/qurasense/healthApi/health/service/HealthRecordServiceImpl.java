package com.qurasense.healthApi.health.service;

import java.util.Date;

import com.qurasense.common.datastore.IdGenerationStrategy;
import com.qurasense.common.repository.EntityRepository;
import com.qurasense.healthApi.health.model.HealthRecord;
import com.qurasense.healthApi.health.repository.HealthRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class HealthRecordServiceImpl implements HealthRecordService {

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private IdGenerationStrategy idGenerationStrategy;

    @Override
    public String saveHealthCustomer(HealthRecord healthRecord) {
        if (healthRecord.getId() == null) {
            healthRecord.setId(idGenerationStrategy.generate(null));
            healthRecord.setParent(entityRepository.getParent(HealthRecord.class));
            healthRecord.setCreateTime(new Date());
            healthRecord.setCreateUserId(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return entityRepository.save(healthRecord);
    }

    @Override
    public HealthRecord findById(String healthCustomerId) {
        return entityRepository.findById(HealthRecord.class, healthCustomerId);
    }

    @Override
    public HealthRecord findByCustomerUserId(String customerUserId) {
        return healthRecordRepository.findByCustomerUserId(customerUserId);
    }

}
