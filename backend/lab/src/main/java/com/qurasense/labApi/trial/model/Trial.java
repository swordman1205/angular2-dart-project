package com.qurasense.labApi.trial.model;

import com.googlecode.objectify.annotation.Entity;
import com.qurasense.common.model.AbstractCreateStamp;
import com.qurasense.common.stereotypes.Nameable;

@Entity
public class Trial extends AbstractCreateStamp implements Nameable {

    private String name;

    private Long compensationAmount;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Long getCompensationAmount() {
        return compensationAmount;
    }

    public void setCompensationAmount(Long compensationAmount) {
        this.compensationAmount = compensationAmount;
    }

}
