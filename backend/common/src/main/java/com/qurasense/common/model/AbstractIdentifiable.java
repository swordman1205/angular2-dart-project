package com.qurasense.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.googlecode.objectify.annotation.Id;
import com.qurasense.common.stereotypes.Identifiable;
import io.swagger.annotations.ApiModelProperty;

public abstract class AbstractIdentifiable implements Identifiable {
    @Id
    @ApiModelProperty(notes = "id", required = true)
    @JsonIgnoreProperties
    private String id;

    public AbstractIdentifiable() {
    }

    public AbstractIdentifiable(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
