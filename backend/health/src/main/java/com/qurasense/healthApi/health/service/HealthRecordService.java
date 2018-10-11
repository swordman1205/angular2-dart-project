package com.qurasense.healthApi.health.service;

import com.qurasense.healthApi.health.model.HealthRecord;

public interface HealthRecordService {

    String saveHealthCustomer(HealthRecord healthRecord);

    HealthRecord findById(String healthCustomerId);

    HealthRecord findByCustomerUserId(String customerUserId);
}
