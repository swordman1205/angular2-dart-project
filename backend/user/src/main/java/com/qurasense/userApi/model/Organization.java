package com.qurasense.userApi.model;

import com.googlecode.objectify.annotation.Entity;
import com.qurasense.common.model.AbstractNameable;
import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(subTypes = {Laboratory.class})
public class Organization extends AbstractNameable {
    private OrganizationType type;
    public OrganizationType getType() {
        return type;
    }
    public void setType(OrganizationType type) {
        this.type = type;
    }
}
