package com.qurasense.userApi.model;

import java.util.List;

import io.swagger.annotations.ApiModel;

@ApiModel(parent = Organization.class)
public class Laboratory extends Organization {

    private List<String> assayIds;

    public List<String> getAssayIds() {
        return assayIds;
    }

    public void setAssayIds(List<String> assayIds) {
        this.assayIds = assayIds;
    }
}
