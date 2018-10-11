package com.qurasense.healthApi.repository;

import java.util.Objects;

import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.healthApi.health.model.HealthRecord;
import com.qurasense.healthApi.health.repository.HealthRecordRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocalHealthRecordRepository extends LocalCustomRepository<HealthRecord> implements HealthRecordRepository {

    @Override
    protected Class<HealthRecord> getEntityClass() {
        return HealthRecord.class;
    }

    @Override
    public HealthRecord findByCustomerUserId(String userId) {
        return getValues().stream().filter(hc -> Objects.equals(userId, hc.getCustomerUserId())).findFirst().get();
    }

}
