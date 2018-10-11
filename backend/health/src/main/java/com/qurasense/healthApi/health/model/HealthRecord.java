package com.qurasense.healthApi.health.model;

import com.google.cloud.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.model.AbstractCreateStamp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "health record")
@Entity
public class HealthRecord extends AbstractCreateStamp {

    @Parent
    @ApiModelProperty(hidden = true)
    private com.google.cloud.datastore.Key parent;

    private String customerId;
    @Index
    private String customerUserId;
    private String trialId;

    public Key getParent() {
        return parent;
    }

    public void setParent(Key parent) {
        this.parent = parent;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(String customerUserId) {
        this.customerUserId = customerUserId;
    }

    public String getTrialId() {
        return trialId;
    }

    public void setTrialId(String trialId) {
        this.trialId = trialId;
    }

    @Override
    public String toString() {
        return "HealthRecord{" +
                "parent=" + parent +
                ", customerId='" + customerId + '\'' +
                ", customerUserId='" + customerUserId + '\'' +
                ", trialId='" + trialId + '\'' +
                '}';
    }
}
