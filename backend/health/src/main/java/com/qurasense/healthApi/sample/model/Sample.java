package com.qurasense.healthApi.sample.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.OnLoad;
import com.qurasense.common.model.AbstractCreateStamp;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Entity
public class Sample extends AbstractCreateStamp {

    private String kitId;

    private String customerId;

    public String getKitId() {
        return kitId;
    }

    public void setKitId(String kitId) {
        this.kitId = kitId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @OnLoad
    void onLoad() {
        if (customerId == null) {
            customerId = getCreateUserId();
            ofy().save().entity(this).now();
        }
    }

}
