package com.qurasense.healthApi.health.repository;

import com.qurasense.healthApi.health.model.HealthInfo;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Repository
@Profile({"emulator", "cloud"})
public class HealthInfoRepositoryImpl implements HealthInfoRepository {

    @Override
    public HealthInfo findHealth(String aUserId) {
        return ofy().load().type(HealthInfo.class).filter("userId =", aUserId).first().now();
    }

}
