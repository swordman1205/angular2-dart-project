package com.qurasense.common.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Index;
import com.qurasense.common.stereotypes.CreateStamp;
import io.swagger.annotations.ApiModelProperty;

public abstract class AbstractCreateStamp extends AbstractIdentifiable implements CreateStamp {

    @Index
    @ApiModelProperty(notes = "createTime", required = true)
    private Date createTime;

    @Index
    @ApiModelProperty(notes = "createUserId", required = true)
    private String createUserId;

    public AbstractCreateStamp() {
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getCreateUserId() {
        return createUserId;
    }

    @Override
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
