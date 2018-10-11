package com.qurasense.userApi.model;

import com.google.cloud.datastore.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.model.AbstractCreateStamp;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class EmailConfirmationToken extends AbstractCreateStamp {

    @Parent
    @ApiModelProperty(hidden = true)
    private Key parent;
    @Index
    private String token;

    private String customerId;

    public Key getParent() {
        return parent;
    }

    public void setParent(Key parent) {
        this.parent = parent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
