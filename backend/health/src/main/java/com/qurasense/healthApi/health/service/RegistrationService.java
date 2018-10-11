package com.qurasense.healthApi.health.service;

import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.model.HealthRecord;

public interface RegistrationService {

    String register(RegistrationData registrationData);

    public static class RegistrationData {
        private HealthInfo healthInfo;
        private HealthRecord healthRecord;

        public HealthInfo getHealthInfo() {
            return healthInfo;
        }

        public void setHealthInfo(HealthInfo healthInfo) {
            this.healthInfo = healthInfo;
        }

        public HealthRecord getHealthRecord() {
            return healthRecord;
        }

        public void setHealthRecord(HealthRecord healthRecord) {
            this.healthRecord = healthRecord;
        }
    }

}
