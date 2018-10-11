package com.qurasense.healthApi.health.repository;

import com.qurasense.common.repository.EntityRepository;
import com.qurasense.healthApi.health.model.HealthRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Repository
@Profile({"emulator", "cloud"})
public class HealthRecordRepositoryImpl implements HealthRecordRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityRepository entityRepository;

    @Override
    public HealthRecord findByCustomerUserId(String userId) {
        return ofy().load().type(HealthRecord.class)
                .ancestor(entityRepository.getParent(HealthRecord.class))
                .filter("customerUserId =", userId)
                .first()
                .now();
    }

}
