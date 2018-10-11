package com.qurasense.labApi.trial.model;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.qurasense.common.model.AbstractIdentifiable;
import io.swagger.annotations.ApiModelProperty;

@Entity
public class SessionStatusChangeToken extends AbstractIdentifiable {

    @Parent
    @ApiModelProperty(hidden = true)
    private com.google.cloud.datastore.Key parent;
    private Ref<TrialSession> session;
    @Index
    private String token;
    private Date finishTime;

    public com.google.cloud.datastore.Key getParent() {
        return parent;
    }

    public void setParent(com.google.cloud.datastore.Key parent) {
        this.parent = parent;
    }

    public String getSessionId() {
        return session.getKey().getName();
    }

    public void setSessionId(String sessionId) {
        this.session = Ref.create(Key.create(TrialSession.class, sessionId));
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
