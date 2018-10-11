package com.qurasense.labApi.trial.model;

import java.util.HashSet;
import java.util.Set;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.model.AbstractCreateStamp;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class TrialSession extends AbstractCreateStamp {

    @Parent
    @ApiModelProperty(hidden = true)
    private com.google.cloud.datastore.Key parent;
    @Index
    private Ref<TrialParticipant> trialParticipant;
    private String pickupNurseId;
    @Index
    private TrialSessionStatus status;
    private boolean compensated;
    private String feedback;
    private Set<String> sampleIds = new HashSet<>();

    public TrialSession() {
        System.out.println();
    }

    public Set<String> getSampleIds() {
        return sampleIds;
    }

    public com.google.cloud.datastore.Key getParent() {
        return parent;
    }

    public void setParent(com.google.cloud.datastore.Key parent) {
        this.parent = parent;
    }

    @ApiModelProperty(hidden = true)
    public TrialParticipant getTrialParticipant() {
        return trialParticipant.get();
    }

    public String getTrialParticipantId() {
        return trialParticipant.getKey().getName();
    }

    public void setTrialParticipantId(String participantId) {
        trialParticipant = Ref.create(Key.create(TrialParticipant.class, participantId));
    }

    public String getPickupNurseId() {
        return pickupNurseId;
    }

    public void setPickupNurseId(String pickupNurseId) {
        this.pickupNurseId = pickupNurseId;
    }

    public TrialSessionStatus getStatus() {
        return status;
    }

    public void setStatus(TrialSessionStatus status) {
        this.status = status;
    }

    public boolean isCompensated() {
        return compensated;
    }

    public void setCompensated(boolean compensated) {
        this.compensated = compensated;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setSampleIds(Set<String> sampleIds) {
        this.sampleIds = sampleIds;
    }
}
