package com.qurasense.healthApi.health.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qurasense.common.model.AbstractIdentifiable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "healt card")
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthCard extends AbstractIdentifiable {

    @ApiModelProperty(notes = "user id", required = true)
    private String userId;

    @ApiModelProperty(notes = "HealthCardType with the values: ENRICH_PROFILE, PAD_ACTION", required = true)
    private HealthCardType type;

    @ApiModelProperty(notes = "date of card create", required = true)
    private LocalDate createDate;

    @ApiModelProperty(notes = "note", required = true)
    private String note;

    public HealthCard(String userId, HealthCardType type, LocalDate createDate, String note) {
        this(null, userId, type, createDate, note);
    }

    public HealthCard(String id, String userId, HealthCardType type, LocalDate creationDate, String note) {
        super(id);
        this.userId = userId;
        this.type = type;
        this.createDate = creationDate;
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HealthCardType getType() {
        return type;
    }

    public void setType(HealthCardType type) {
        this.type = type;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
