package com.qurasense.labApi.trial.model;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.model.AbstractIdentifiable;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

@Entity
public class TrialParticipant extends AbstractIdentifiable {

    @Parent
    @ApiModelProperty(hidden = true)
    private com.google.cloud.datastore.Key parent;
    @Index
    private String customerId;
    @Index
    private String nurseId;
    @Index
    private ParticipantStatus status;
    @Index
    private Ref<Trial> trial;

    private Date sentKitsTime;
    private boolean hasKits;

    public com.google.cloud.datastore.Key getParent() {
        return parent;
    }

    public void setParent(com.google.cloud.datastore.Key parent) {
        this.parent = parent;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getNurseId() {
        return nurseId;
    }

    public void setNurseId(String nurseId) {
        this.nurseId = nurseId;
    }

    public ParticipantStatus getStatus() {
        return status;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }

    public String getTrialId() {
        return trial.getKey().getName();
    }

    public void setTrialId(String trialId) {
        trial = Ref.create(Key.create(Trial.class, trialId));
    }

    @ApiIgnore
    public Trial getTrial() {
        return trial.get();
    }

    public Date getSentKitsTime() {
        return sentKitsTime;
    }

    public void setSentKitsTime(Date sentKitsTime) {
        this.sentKitsTime = sentKitsTime;
    }

    public boolean isHasKits() {
        return hasKits;
    }

    public void setHasKits(boolean hasKits) {
        this.hasKits = hasKits;
    }

}
