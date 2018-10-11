package com.qurasense.healthApi.repository;

import java.util.Objects;

import com.qurasense.common.repository.local.LocalCustomRepository;
import com.qurasense.healthApi.health.model.HealthInfo;
import com.qurasense.healthApi.health.repository.HealthInfoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocalHealthInfoRepository extends LocalCustomRepository<HealthInfo> implements HealthInfoRepository {

    @Override
    protected Class<HealthInfo> getEntityClass() {
        return HealthInfo.class;
    }

    @Override
    public HealthInfo findHealth(String aUserId) {
        return getValues().stream().filter(hi -> Objects.equals(hi.getUserId(), aUserId)).findFirst().orElse(null);
    }

}
