package com.qurasense.healthApi.health.repository;


import com.qurasense.healthApi.health.model.HealthInfo;

public interface HealthInfoRepository {
    HealthInfo findHealth(String aUserId);
}
