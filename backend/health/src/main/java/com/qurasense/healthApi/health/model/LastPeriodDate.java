package com.qurasense.healthApi.health.model;

import java.util.Date;

import io.swagger.annotations.ApiModel;

@ApiModel
public  class LastPeriodDate {
    private Date lastPeriodDate;

    public Date getLastPeriodDate() {
        return lastPeriodDate;
    }

    public void setLastPeriodDate(Date lastPeriodDate) {
        this.lastPeriodDate = lastPeriodDate;
    }
}