package com.qurasense.userApi.model;

import java.util.Date;

public class CustomerStatus {

    private CustomerStatusType type;
    private Date time;

    public CustomerStatus() {
    }

    public CustomerStatus(CustomerStatusType type, Date time) {
        this.type = type;
        this.time = time;
    }

    public CustomerStatusType getType() {
        return type;
    }

    public void setType(CustomerStatusType type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
