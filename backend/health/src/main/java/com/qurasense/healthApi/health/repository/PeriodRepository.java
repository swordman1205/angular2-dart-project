package com.qurasense.healthApi.health.repository;

import java.util.Date;

public interface PeriodRepository {

    Date getLastPeriodDate(Long aUserId);

    Long savePeriod(Long userId, Date aDate, Date registrationDate);

}
