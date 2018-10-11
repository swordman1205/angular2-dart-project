package com.qurasense.healthApi.sample.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.cloud.datastore.Key;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Subclass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
@Subclass(index=true)
public class StripSample extends Sample {

    @Parent
    @ApiModelProperty(hidden = true)
    private Key sampleList;

    private Date padStartTime;
    private Date padRemoveTime;
    private Date periodStartDate;
    private String padRemovePictureBlobName;
    private Date padRemovePictureTime;
    private String stripRemovePictureBlobName;
    private Date stripRemovePictureTime;
    private Date padCollectedTime;
    private BleedIntensity bleedIntensity;
    private List<PrimaryActivity> primaryActivities = new ArrayList<>();
    private SaturateKnowing padSaturation;
    private ComfortLevel padComfort;
    private String padFeedback;
    private Date updateTime;
    private Long stepNumber;
    @Index
    private SampleStatus status;

    public Key getSampleList() {
        return sampleList;
    }

    public void setSampleList(Key sampleList) {
        this.sampleList = sampleList;
    }

    public Date getPadStartTime() {
        return padStartTime;
    }

    public void setPadStartTime(Date padStartTime) {
        this.padStartTime = padStartTime;
    }

    public Date getPadRemoveTime() {
        return padRemoveTime;
    }

    public void setPadRemoveTime(Date padRemoveTime) {
        this.padRemoveTime = padRemoveTime;
    }

    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public String getPadRemovePictureBlobName() {
        return padRemovePictureBlobName;
    }

    public void setPadRemovePictureBlobName(String padRemovePictureBlobName) {
        this.padRemovePictureBlobName = padRemovePictureBlobName;
    }

    public Date getPadRemovePictureTime() {
        return padRemovePictureTime;
    }

    public void setPadRemovePictureTime(Date padRemovePictureTime) {
        this.padRemovePictureTime = padRemovePictureTime;
    }

    public String getStripRemovePictureBlobName() {
        return stripRemovePictureBlobName;
    }

    public void setStripRemovePictureBlobName(String stripRemovePictureBlobName) {
        this.stripRemovePictureBlobName = stripRemovePictureBlobName;
    }

    public Date getStripRemovePictureTime() {
        return stripRemovePictureTime;
    }

    public void setStripRemovePictureTime(Date stripRemovePictureTime) {
        this.stripRemovePictureTime = stripRemovePictureTime;
    }

    public Date getPadCollectedTime() {
        return padCollectedTime;
    }

    public void setPadCollectedTime(Date padCollectedTime) {
        this.padCollectedTime = padCollectedTime;
    }

    public BleedIntensity getBleedIntensity() {
        return bleedIntensity;
    }

    public void setBleedIntensity(BleedIntensity bleedIntensity) {
        this.bleedIntensity = bleedIntensity;
    }

    public List<PrimaryActivity> getPrimaryActivities() {
        return primaryActivities;
    }

    public void setPrimaryActivities(List<PrimaryActivity> primaryActivities) {
        this.primaryActivities = primaryActivities;
    }

    public SaturateKnowing getPadSaturation() {
        return padSaturation;
    }

    public void setPadSaturation(SaturateKnowing padSaturation) {
        this.padSaturation = padSaturation;
    }

    public ComfortLevel getPadComfort() {
        return padComfort;
    }

    public void setPadComfort(ComfortLevel padComfort) {
        this.padComfort = padComfort;
    }

    public String getPadFeedback() {
        return padFeedback;
    }

    public void setPadFeedback(String padFeedback) {
        this.padFeedback = padFeedback;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Long stepNumber) {
        this.stepNumber = stepNumber;
    }

    public SampleStatus getStatus() {
        return status;
    }

    public void setStatus(SampleStatus status) {
        this.status = status;
    }
}
