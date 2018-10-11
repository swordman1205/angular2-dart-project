package com.qurasense.healthApi.health.repository;

import com.qurasense.healthApi.health.model.HealthRecord;

public interface HealthRecordRepository {

    HealthRecord findByCustomerUserId(String userId);

}
