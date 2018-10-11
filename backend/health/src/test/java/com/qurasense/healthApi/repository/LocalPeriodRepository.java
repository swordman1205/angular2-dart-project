package com.qurasense.healthApi.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.qurasense.healthApi.health.repository.PeriodRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LocalPeriodRepository implements PeriodRepository {

    private Map<Long, Date> repo = new HashMap<>();

    @Override
    public Date getLastPeriodDate(Long aUserId) {
        return repo.get(aUserId);
    }

    @Override
    public Long savePeriod(Long userId, Date aDate, Date registrationDate) {
        repo.put(userId, aDate);
        return 1l;
    }
}
